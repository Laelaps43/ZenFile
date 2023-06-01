package org.zenfile.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zenfile.mapper.StorageSourceConfigMapper;
import org.zenfile.model.storage.entity.StorageSourceConfig;
import org.zenfile.utils.RedisStorageUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class StorageSourceConfigService {
    @Resource
    StorageSourceConfigMapper storageSourceConfigMapper;

    @Resource
    RedisStorageUtils redisStorageUtils;
    public List<StorageSourceConfig> getStorageSourceConfigByStorageId(Long storageSourceId) {
        List<StorageSourceConfig> configList =  redisStorageUtils.getStorageSourceConfigListByStorageId(storageSourceId);
        if (configList.isEmpty()){
            configList = storageSourceConfigMapper.getStorageSourceConfigByStorageId(storageSourceId);
            log.debug("未从Redis总获取StorageSource - {}数据,从数据库中获取到数据为{}", storageSourceId, configList);
            redisStorageUtils.setStorageSourceConfigListByStorageId(storageSourceId, configList);
        }
        return configList;
    }

}
