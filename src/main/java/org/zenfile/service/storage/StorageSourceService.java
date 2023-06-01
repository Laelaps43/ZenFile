package org.zenfile.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zenfile.mapper.StorageSourceMapper;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.enums.StorageTypeEnum;
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
}
