package org.zenfile.service.storage;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zenfile.convert.StorageSourceConvert;
import org.zenfile.exception.UnKnownOperationException;
import org.zenfile.exception.storageSource.InvalidStorageSourceKeyException;
import org.zenfile.mapper.StorageSourceMapper;
import org.zenfile.model.storage.dto.StorageSourceAllParamDto;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.entity.StorageSourceConfig;
import org.zenfile.model.storage.enums.StorageTypeEnum;
import org.zenfile.model.storage.request.StorageSourceRequest;
import org.zenfile.utils.CodeMsg;
import org.zenfile.utils.RedisStorageUtils;

import javax.annotation.Resource;
import java.util.Date;
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
        log.debug("根据存储源Key，获取到存储源Id为{}", storageID);
        StorageSource storageSource = getStorageSourceById(storageID);
        log.debug("根据存储源Id{}", storageSource);
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

    /**
     * 更改或者更新存储源
     */
    @Transactional
    public StorageSource saveStorageSource(StorageSourceRequest request) {
        log.info("保存或修改存储源Key：{}, newKey: {}, name：{}, type：{}", request.getKey(), request.getNewKey(), request.getName()
                            , request.getType());
        StorageSource storageSource = storageSourceConvert.storageSourceRequestToStorageSource(request);
        if(request.getKey() == null && request.getNewKey() == null){
            throw new UnKnownOperationException(CodeMsg.UNKNOWN_OPERATION);
        }
        // key不为空，为更新存储源
        if(request.getKey() != null){
            Boolean isExist = existKey(storageSource.getKey());
            if (!isExist) {
                // 存在不存在这个Key，但是为更新这个存储源，抛出异常
                String message = StrUtil.format("所执行的操作是更新存储源，但是{}（key）不存在。", request.getKey());
                throw new InvalidStorageSourceKeyException(CodeMsg.KEY_UPDATE_CONFLICT, null, message);
            }
            // 有对应的Key，获取对应的Id
            Long storageIdByKey = getStorageIdByKey(storageSource.getKey());
            storageSource.setId(storageIdByKey);
        }
        // 更新操作的时候newKey可能为空，对于插入操作，不可能为空
        if(request.getNewKey() != null){
            storageSource.setKey(request.getNewKey());
        }
        log.debug("修改或更新StorageSource {}", storageSource);
        storageSource = insertOrUpdateStorageSource(storageSource);
        // 删除Redis中key到Id的映射
        redisStorageUtils.delStorageSourceKeyToId(request.getKey());
        List<StorageSourceConfig> configList = storageSourceConfigService.toStroageSourceConfigList(storageSource, request);
        storageSourceConfigService.batchSaveConfig(storageSource, configList);
        redisStorageUtils.delStorageSourceConfigById(storageSource.getId());

        log.info("保存存储源参数成功，存储源：id：{}， name：{}, config size: {}", storageSource.getId(),
                storageSource.getName(), configList.size());
        return storageSource;
    }

    /**
     * 根据StorageSource来决定是否新增存储源或者更新存储元
     * @param storageSource 存储源
     */
    @Transactional
    public StorageSource insertOrUpdateStorageSource(StorageSource storageSource){
        if(storageSource.getId() == null){
            storageSource.setCreateTime(new Date());
            // TODO 没有实现用户功能
            storageSource.setCreateUser(1L);
            // 新增存储源
            storageSourceMapper.insertStorageSource(storageSource);
        }else{
            // 已存在存储源，需要清除Redis对存储源的缓存
            storageSourceMapper.updateStorageSourceById(storageSource);
            redisStorageUtils.delStorageSourceById(storageSource);
        }
        return storageSource;
    }
}
