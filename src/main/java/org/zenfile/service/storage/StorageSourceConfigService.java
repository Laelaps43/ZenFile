package org.zenfile.service.storage;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zenfile.mapper.StorageSourceConfigMapper;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.entity.StorageSourceConfig;
import org.zenfile.model.storage.request.StorageSourceRequest;
import org.zenfile.service.storage.context.StorageSourceContext;
import org.zenfile.utils.RedisStorageUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageSourceConfigService {
    @Resource
    StorageSourceConfigMapper storageSourceConfigMapper;

    @Resource
    RedisStorageUtils redisStorageUtils;

    public List<StorageSourceConfig> getStorageSourceConfigByStorageId(Long storageSourceId) {
        List<StorageSourceConfig> configList =  redisStorageUtils.getStorageSourceConfigListByStorageId(storageSourceId);
        log.debug("获取到存储源Id {}的参数个数为{}", storageSourceId, configList.size());
        if (configList.isEmpty()){
            configList = storageSourceConfigMapper.getStorageSourceConfigByStorageId(storageSourceId);
            log.debug("未从Redis总获取StorageSource - {}数据,从数据库中获取到数据为{}", storageSourceId, configList);
            redisStorageUtils.setStorageSourceConfigListByStorageId(storageSourceId, configList);
        }
        return configList;
    }

    /**
     * 将传递过来的StorageSource对象中的 对应存储源的参数信息
     * @param storageSource 存储源信息
     * @param request 对应参数
     */
    public List<StorageSourceConfig> toStroageSourceConfigList(StorageSource storageSource, StorageSourceRequest request) {
        List<String> paramList = StorageSourceContext.getStorageSourceParamListByType(storageSource.getType());
        List<StorageSourceConfig> storageSourceConfigList = paramList.stream()
                .map(s -> {
                    String fieldValue = (String) ReflectUtil.getFieldValue(request, s);
                    return StorageSourceConfig.builder()
                            .title(s)
                            .value(fieldValue).build();
                }).collect(Collectors.toList());
        return storageSourceConfigList;
    }

    @Transactional
    public void batchSaveConfig(StorageSource storageSource, List<StorageSourceConfig> configList) {
        storageSourceConfigMapper.deleteByStorageId(storageSource.getId());
        log.info("更新存储源 ID 为 {}的配置 {} 条", storageSource.getId(), configList.size());
        storageSourceConfigMapper.batchSaveConfig(storageSource.getId(), configList);
        log.info("更新存储源 ID 为 {}的配置 {} 条完成。", storageSource.getId(), configList.size());
    }
}
