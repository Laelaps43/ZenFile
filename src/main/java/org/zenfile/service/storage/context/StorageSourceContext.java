package org.zenfile.service.storage.context;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;
import org.zenfile.exception.storageSource.InitializeStorageSourceException;
import org.zenfile.exception.storageSource.InvalidStorageSourceException;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.entity.StorageSourceConfig;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.model.storage.param.StorageParam;
import org.zenfile.service.storage.StorageSourceConfigService;
import org.zenfile.service.storage.StorageSourceService;
import org.zenfile.service.storage.base.AbstractBaseFileService;
import org.zenfile.utils.CodeMsg;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class StorageSourceContext implements ApplicationContextAware {

    @Resource
    StorageSourceService storageSourceService;

    @Resource
    StorageSourceConfigService storageSourceConfigService;

    /**
     * 所有的存储源对象
     */
    private static Map<String, AbstractBaseFileService> storageSourceFileServiceMap;

    /**
     * 存储每个存储源Service的参数值
     */
    private final Map<Class<?>, Map<String, Field>> PARAM_CLASS_FIELD_NAME_MAP_CACHE = new HashMap<>();

    /**
     * 已经初始化的Service缓存
     */
    private static final Map<Long, AbstractBaseFileService<StorageParam>> SERVICE_MAP = new ConcurrentHashMap<>();


    /**
     * 当项目启动时，自动进行存储源服务类的配置
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
         storageSourceFileServiceMap = applicationContext.getBeansOfType(AbstractBaseFileService.class);
        List<StorageSource> storageSources = storageSourceService.getAllStorageSource();
        for (StorageSource storageSource : storageSources) {
            try {
                init(storageSource);
                log.info("初始化存储源{}成功，存储源类型{},存储源名称{}", storageSource.getKey(), storageSource.getType(), storageSource.getName());
            }catch (Exception e){
                log.error("初始化存储源{}失败，存储源类型{},存储源名称{}", storageSource.getKey(), storageSource.getType(), storageSource.getName());
            }
        }
    }

    /**
     * 初始化指定的存储元Service
     */
    public void init(StorageSource storageSource){
       log.debug("开始初始化{}存储源", storageSource.getKey());

        Long storageSourceId = storageSource.getId();
        String storageSourceName = storageSource.getName();
        log.debug("开始获取FileService");
        AbstractBaseFileService<StorageParam> fileService= getInitStorageSourceBeanByStorageId(storageSourceId);
        log.debug("获取到FileService为{}", fileService);

        // 当没有寻找到对应的Service抛出异常
        if(fileService == null){
            throw new InvalidStorageSourceException(storageSourceId);
        }

        fileService.setStorageId(storageSourceId);
        fileService.setName(storageSourceName);

        // 获取对应的存储源参数
        StorageParam storageParam = getInitParam(storageSourceId, fileService);
        log.debug("获取到指定参数类型为{}", storageParam.getClass());
        fileService.setParam(storageParam);

        // 对应的存储源Service初始化
        log.debug("开始对存储源{}，开始初始化", fileService);
        fileService.init();
        fileService.testConnection();

       SERVICE_MAP.put(storageSourceId, fileService);
    }

    /**
     * 获取对应存储源的Param参数
     * @param storageSourceId 存储源Id
     * @param fileService 存储源Service
     * @return 获取到的参数
     */
    public StorageParam getInitParam(Long storageSourceId, AbstractBaseFileService<StorageParam> fileService) {
        // 获取AbstractBaseFileService类的实际实现类，一般是在impl文件下的类
        Class<?> targetClass = AopUtils.getTargetClass(fileService);
        log.debug("targetClass是{}", targetClass);
        // 获取targetClass的实际参数类型Class
//        Class<?> beanClass = ResolvableType.forClass(targetClass).getGeneric(0).asMap().resolve();
        ResolvableType resolvableType = ResolvableType.forClass(targetClass).getSuperType();
        log.debug("toMap{}", resolvableType.asMap());
        Class<?> beanClass = resolvableType.getGeneric(0).resolve();
        log.debug("获取{}的泛型参数类型为{}", targetClass.getName(), beanClass);


        Map<String, Field> fieldMap = new HashMap<>();
        if(PARAM_CLASS_FIELD_NAME_MAP_CACHE.containsKey(beanClass)){
            fieldMap = PARAM_CLASS_FIELD_NAME_MAP_CACHE.get(beanClass);
        }else {
            // 获取所有的属性
            Field[] fields = ReflectUtil.getFieldsDirectly(beanClass, true);

            // 将所有的Field放入到Map中
            for (Field field : fields) {
                String name = field.getName();
                log.debug("获取到属性为{}", name);
                if(fieldMap.containsKey(name)){
                    continue;
                }
                fieldMap.put(name, field);
            }
            PARAM_CLASS_FIELD_NAME_MAP_CACHE.put(beanClass, fieldMap);
        }

        // 获取BeanClass的示例化对象
        StorageParam storageParam = ReflectUtil.newInstance(beanClass.getName());

        List<StorageSourceConfig> storageSourceConfigs = storageSourceConfigService.getStorageSourceConfigByStorageId(storageSourceId);

        for (StorageSourceConfig storageSourceConfig : storageSourceConfigs) {
            String title = storageSourceConfig.getTitle();
            String value = storageSourceConfig.getValue();

            try{
                // 为StorageParam相关的属性赋值
                Field field = fieldMap.get(title);
                log.debug("{} 属性 {} {}", storageParam.getClass(), title, value);
                ReflectUtil.setFieldValue(storageParam, field, value);
                log.debug("storageParam {}", storageParam);
            }catch (Exception exception){
               String errMsg = StrUtil.format("初始化存储元{}，为{}赋值失败，值为{}", storageSourceId, title, value);
               throw new InitializeStorageSourceException(CodeMsg.INITIALIZE_STORAGE_SOURCE,storageSourceId, errMsg);
            }
        }
        log.debug("返回获取到的存储源参数{}", storageParam);
        return storageParam;
    }

    /**
     * 根据指定的存储源类型返回一个初始化状态的 Service，获取bean的方法是SpringUtil.getBean()
     */
    public AbstractBaseFileService<StorageParam> getInitStorageSourceBeanByStorageId(Long storageId){
        log.debug("开始获取值指定Id的存储源类型");
        StorageTypeEnum storageTypeEnum = storageSourceService.getStorageTypeById(storageId);
        log.debug("获取到的是StorageTypeEnum为{}", storageTypeEnum.getValue());
        for (AbstractBaseFileService<?> value : storageSourceFileServiceMap.values()) {
            log.debug("{} - {}",value.getStorageTypeEnum(), storageTypeEnum);
           if(Objects.equals(value.getStorageTypeEnum(), storageTypeEnum)) {
               log.debug("成功寻找到对应的FileService{}", value);
               // 获取到对应的Bean
               return SpringUtil.getBean(value.getClass());
           }
        }
        return null;
    }

}
