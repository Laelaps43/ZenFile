package org.zenfile.service.storage.base;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.zenfile.exception.storageSource.InitializeStorageSourceException;
import org.zenfile.model.file.dto.FileItemResult;
import org.zenfile.model.file.entity.FileItem;
import org.zenfile.model.storage.param.StorageParam;
import org.zenfile.utils.CodeMsg;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public abstract class AbstractBaseFileService<P extends StorageParam> implements BaseFileService {


    /**
     * 对响应的AbstractBaseFileService的缓存
     */
    private static final Map<Class<? extends AbstractBaseFileService>, List<String>> STORAGE_SOURCE_PARAM_CACHE = new ConcurrentHashMap<>();

    /**
     * 存储源配置
     */
    private P param;

    /**
     * 是否初始化成功
     */
    private boolean isInitialized = false;

    /**
     * 存储源Id
     */
    private Long storageId;

    /**
     * 存储源名称
     */

    private String name;

    /**
     * 初始化存储源
     */
    public abstract void init();

    public abstract List<FileItem> listFileItemByStorage(String folderPath) throws Exception;

    /**
     * 测试链接是否成功，不成功抛出异常
     */
    public void testConnection(){
        try {
            fileList("/");
            // 系统初始化成功
            log.debug("系统初始化成功");
            isInitialized = true;
        } catch (Exception e) {
            throw new InitializeStorageSourceException(CodeMsg.INITIALIZE_STORAGE_SOURCE, storageId, "初始化异常：" + e.getMessage());
        }
    }


    /**
     * 获取单个文件信息
     * @param pathAndName 文件路径+文件名称
     */
    public abstract FileItemResult getFileItem(String pathAndName) throws FileNotFoundException;

    public abstract boolean newFolder(String path, String name);
    public abstract boolean deleteFile(String path, String name);

    public abstract boolean deleteFolder(String path, String name);

    public abstract boolean renameFile(String path, String name);

    public abstract boolean renameFolder(String path, String name);

    /**
     * 获取上传路径
     * @param path 上传的路径
     * @param name 上传文件名称
     * @param size 上传文件大小
     * @return 上传的url
     */
    public abstract String getUploadUrl(String path, String name, Long size) ;


    public void setStorageId(Long storageId){
        if(this.storageId != null){
            throw new IllegalStateException("请勿重复初始化存储源");
        }
        this.storageId = storageId;
    }

    public void setName(String name){
        if(this.name != null){
            throw new IllegalStateException("请勿重复初始化存储源");
        }
        this.name = name;
    }

    public void setParam(P param){
        if(this.param != null){
            throw new IllegalStateException("请勿重复初始化存储源");
        }

        this.param = param;
    }

    /**
     * 判断是否初始化成功
     */
    public boolean getIsInitialized(){
        return this.isInitialized;
    }

    /**
     * 获取存储源的存储信息，主要为以使用大小，以及存储源大小
     */
    public abstract List<Long> getSourceStorageInfo();

    /**
     * 获取当前存储元需要的所有参数，继承在StorageParam
     * @return 所有的参数名称
     */
    public List<String> getStorageSourceParamList(){
        Class<? extends AbstractBaseFileService> thisClass = this.getClass();
        if(STORAGE_SOURCE_PARAM_CACHE.containsKey(thisClass)){
            return STORAGE_SOURCE_PARAM_CACHE.get(thisClass);
        }

        ArrayList<String> paramList = new ArrayList<>();
        // 获取泛型中参数的类型
        Class<?> beanClass = ResolvableType.forClass(thisClass).getSuperType().getGeneric(0).resolve();
        // 获取所有参数中的属性
        Field[] fields = ReflectUtil.getFieldsDirectly(beanClass, true);
        Arrays.stream(fields).forEach(field -> paramList.add(field.getName()));
        // 添加到缓存当中
        STORAGE_SOURCE_PARAM_CACHE.put(thisClass, paramList);
        return paramList;
    }
}
