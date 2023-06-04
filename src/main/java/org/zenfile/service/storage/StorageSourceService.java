package org.zenfile.service.storage;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zenfile.convert.StorageSourceConvert;
import org.zenfile.exception.storageSource.InvalidStorageSourceKeyException;
import org.zenfile.mapper.StorageSourceMapper;
import org.zenfile.model.storage.dto.StorageSourceAllParamDto;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.entity.StorageSourceConfig;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.utils.CodeMsg;
import org.zenfile.utils.RedisStorageUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 存储源配置类
 */
@Service
@Slf4j
public class StorageSourceService {

    @Resource
    RedisStorageUtils redisStorageUtils;

    @Resource
    StorageSourceMapper storageSourceMapper;

    @Resource
    StorageSourceConfigService storageSourceConfigService;

    @Resource
    StorageSourceConvert storageSourceConvert;

    /**
     * 根据存储Id获取存储源Key
     * @param storageId 存储源Id
     * @return 存储源key
     */
    public String getStorageKeyById(Long storageId) {
        StorageSource storageSource = redisStorageUtils.getStorageSourceById(storageId);
        if(storageSource== null){
            storageSource = storageSourceMapper.getStorageSourceById(storageId);
            redisStorageUtils.setStorageSource(storageSource);
        }
        return storageSource.getKey();
    }

    /**
     * 获取所有的存储源，但并不从Redis中获取，因为Redis中可能并不完整
     */
    public List<StorageSource> getAllStorageSource() {
        List<StorageSource> storageSources = storageSourceMapper.getAllStorageSource();
        return storageSources;
    }

    /**
     * 根据存储源Id获取响应存储源的类型
     * @param storageId 存储源Id
     * @return 存储源类型
     */
    public StorageTypeEnum getStorageTypeById(Long storageId) {
        StorageSource storageSource = redisStorageUtils.getStorageSourceById(storageId);
        log.debug("从Redis中获取到数据为{}", storageSource);
        if(storageSource == null){
            storageSource = storageSourceMapper.getStorageSourceById(storageId);
            log.debug("没有从Redis中到相应数据，从数据库中获取相应数据，获取到的数据为{}", storageSource);
            redisStorageUtils.setStorageSource(storageSource);
        }
        return storageSource.getType();
    }

    /**
     * 根据key来获取存储源Id
     * @param key 存储Key
     * @return 存储Id
     */
    public Long getStorageIdByKey(String key) {
        StorageSource storageSource = redisStorageUtils.getStorageSourceByKey(key);
        if (storageSource == null){
            storageSource = storageSourceMapper.getStorageSourceByKey(key);
            if(storageSource == null)
                throw new InvalidStorageSourceKeyException(CodeMsg.KEY_INVALID, null, "非法Key：" + key);
            log.debug("没有从Redis中到相应数据，从数据库中通过key:{}获取相应数据，获取到的数据为{}", key, storageSource);
            redisStorageUtils.setStorageSourceByKey(storageSource);
        }
        return storageSource.getId();
    }

    /**
     * 根据指定Id获取存储源
     * @param storageId 存储源Id
     */
    public StorageSource getStorageSourceById(Long storageId){
        StorageSource storageSource = redisStorageUtils.getStorageSourceById(storageId);
        if(storageSource == null) {
            storageSource = storageSourceMapper.getStorageSourceById(storageId);
            redisStorageUtils.setStorageSource(storageSource);
        }
        return storageSource;
    }
    /**
     * 获取指定存储源的所有数据，包括存储源的所有配置，返回存储源DTO
     * @param storageKey 存储源Key
     * @return 存储源DTO
     */
    public StorageSourceDto getStorageItemByKey(String storageKey) {
        Long storageID = getStorageIdByKey(storageKey);
        StorageSource storageSource = getStorageSourceById(storageID);
        List<StorageSourceConfig> configList = storageSourceConfigService.getStorageSourceConfigByStorageId(storageID);
        StorageSourceAllParamDto storageSourceAllParamDto = new StorageSourceAllParamDto();
        configList.forEach(storageSourceConfigService ->{
            ReflectUtil.setFieldValue(storageSourceAllParamDto, storageSourceConfigService.getTitle(), storageSourceConfigService.getValue());
        });
        return storageSourceConvert.getStorageSourceDto(storageSource, storageSourceAllParamDto);
    }

    /**
     * 判断给定的Key是否存储，先从Redis中获取，不存在再从数据中查找
     * @param storageKey 存储Key
     */
    public Boolean existKey(String storageKey) {
        Long storageId = redisStorageUtils.getStorageSourceIdByKey(storageKey);
        if(storageId != null) return true;
        StorageSource storageSource = storageSourceMapper.getStorageSourceByKey(storageKey);
        return storageSource != null;
    }
}
