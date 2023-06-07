package org.zenfile.service.storage.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zenfile.convert.FileConvert;
import org.zenfile.exception.file.SaveFileToDateBaseException;
import org.zenfile.exception.storageSource.DuplicateDriverException;
import org.zenfile.exception.storageSource.InitializeStorageSourceException;
import org.zenfile.model.file.dto.FileItemResult;
import org.zenfile.model.file.entity.FileItem;
import org.zenfile.model.file.enums.FileTypeEnum;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.model.storage.param.LocalParam;
import org.zenfile.model.utils.FileStoreInfo;
import org.zenfile.service.file.FileService;
import org.zenfile.service.storage.base.AbstractProxyTransferService;
import org.zenfile.utils.CodeMsg;
import org.zenfile.utils.MachineInfoUtils;
import org.zenfile.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 本地存储源
 */
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 多例模式
@Slf4j
public class BaseLocalServiceImpl extends AbstractProxyTransferService<LocalParam> {

    @javax.annotation.Resource
    FileService fileService;

    @javax.annotation.Resource
    FileConvert fileConvert;

    @Override
    public void init() {
        log.debug("{}初始化中...", getName());
        String path = getParam().getFilePath();
        log.debug("初始化路径为{}", path);
        File file = new File(path);
        if(!file.exists()){
            throw new InitializeStorageSourceException(CodeMsg.INITIALIZE_STORAGE_SOURCE,getStorageId(),
                    StrUtil.format("文件路径: {} 不存在，请检查是否正确。", file.getAbsolutePath()));
        }

        // TODO 保存到数据库
        try {
            saveFileToDataBase("/");
        } catch (Exception e) {
            throw new SaveFileToDateBaseException("保存到数据库失败");
        }
        log.info("存储源{}, 初始化成功!", getStorageId());

    }


    /**
     * 获取指定路径下的文件，查询数据库，对外提供接口, 这里不能去获取[/] 因为他不是一个文件
     * @param pathAndName 文件路径+文件名称
     * @return 返回查询后的结果
     */
    @Override
    public FileItemResult getFileItem(String pathAndName) throws FileNotFoundException {
        checkPathSecurity(pathAndName);
        // 从数据库中获取数据
        FileItem fileItemStorage = fileService.getFileItemStorage(pathAndName, getStorageId());
        if(fileItemStorage == null){
            throw new FileNotFoundException(StrUtil.format("存储源{}文件{}不存在", getStorageId(), pathAndName));
        }

        // 将一个fileItem对象转变为FileItemResult
        FileItemResult fileItemResult = fileConvert.fileItemToResult(fileItemStorage);
        String url = getDownloadUrl(pathAndName);
        log.debug("获取到{}文件的下载地址为{}", pathAndName, url);
        fileItemResult.setUrl(url);
        return fileItemResult;
    }

    @Override
    public boolean newFolder(String path, String name) {
        return false;
    }

    @Override
    public boolean deleteFile(String path, String name) {
        return false;
    }

    @Override
    public boolean deleteFolder(String path, String name) {
        return false;
    }

    @Override
    public boolean renameFile(String path, String name) {
        return false;
    }

    @Override
    public boolean renameFolder(String path, String name) {
        return false;
    }

    /**
     * 获取本地分区的总空间以及使用空间
     * @return 返回一个列表 第一个为总大小，第二个为使用大小
     */
    @Override
    public List<Long> getSourceStorageInfo() {
        List<FileStoreInfo> machineStorageInfo = MachineInfoUtils.getMachineStorageInfo();
        List<FileStoreInfo> filterList = machineStorageInfo.parallelStream().filter(fileStoreInfo -> fileStoreInfo.getName().equals(getParam().getDriver())).toList();
        if(filterList.size() > 1){
            throw new DuplicateDriverException(CodeMsg.DUPLICATE_DRIVER, getStorageId());
        }
        ArrayList<Long> info = new ArrayList<>();
        info.add(filterList.get(0).getTotalSpace());
        info.add(filterList.get(0).getTotalSpace() - filterList.get(0).getFreeSpace());
        return info;
    }

    @Override
    public void uploadFile(String pathAndName, InputStream inputStream) {

    }

    @Override
    public ResponseEntity<Resource> downloadToStream(String pathAndName) {
        return null;
    }

    /**
     * 获取指定文件夹下的文件和文件夹，这只在相应的类有效，并不对外提供服，所有的数据将会被保存到数据库
     * 用来填充数据库
     * @param folderPath 给定的文件夹路径
     */
    @Override
    public List<FileItem> listFileItemByStorage(String folderPath) throws Exception {
        checkPathSecurity(folderPath);
        String folder = StringUtils.concat(getParam().getFilePath(), folderPath);
        File file = new File(folder);
        if(!file.exists()){
            throw new FileNotFoundException(StrUtil.format("文件{}不存在", folderPath));
        }

        List<FileItem> fileItems = new ArrayList<>();
        File[] files = file.listFiles();

        if(files == null){
            return fileItems;
        }


        for (File item : files) {
            fileItems.add(fileToFileItem(item, folderPath));
        }
        return fileItems;
    }

    /**
     * 获取指定文件夹下的所有文件和文件，从数据库中获取，提供对外接口
     * @param folderPath 给定的文件夹路径
     */
    @Override
    public List<FileItem> fileList(String folderPath) throws Exception {

        return null;
    }

    /**
     * 将一个File对象转换成 FileItem对象，
     * 只在初始化时候可以使用，不对外提供接口，
     * 所有数据将会被保存到数据库
     * @param item File对象
     * @param folderPath 当前路径
     */
    private FileItem fileToFileItem(File item, String folderPath) {
        FileItem fileItem = new FileItem();
        fileItem.setName(item.getName());
        fileItem.setPath(folderPath);
        fileItem.setStorageId(getStorageId());
        fileItem.setCreateTime(new Date(item.lastModified()));
        fileItem.setSize(item.isDirectory() ? null : item.length());
        fileItem.setType(item.isDirectory() ? FileTypeEnum.FOLDER : FileTypeEnum.FILE);
        return fileItem;
    }

    /**
     * 路径检查，路径中不能包含有 `../` `./`来非法获取上一层数据
     * @param paths 出入的所有路径
     */
    private void checkPathSecurity(String ...paths) {
        for (String path : paths) {
            if(StrUtil.containsAny(path, "../", "..\\")){
                throw new IllegalArgumentException(StrUtil.format("路径{}存在安全隐患。", path));
            }
        }
    }

    /**
     * 当前Service中的存储源类型
     */
    @Override
    public StorageTypeEnum getStorageTypeEnum() {
        return StorageTypeEnum.LOCAL;
    }
}
