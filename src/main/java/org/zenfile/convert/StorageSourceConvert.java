package org.zenfile.convert;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.zenfile.model.storage.dto.StorageSourceAllParamDto;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
import org.zenfile.model.storage.request.StorageSourceRequest;
import org.zenfile.model.storage.result.StorageSourceResult;

import java.util.List;

/**
 * 存储源转换类
 */
@Component
@Mapper(componentModel = "spring")
public interface StorageSourceConvert {

    /**
     * 将StorageSource List 装换成 StorageSourceResult List
     */
    List<StorageSourceResult> storageSourceToResult(List<StorageSource> storageSources);

    /**
     * 将存储源和存储源所有的参数转换成 StorageSourceDto
     */
    StorageSourceDto getStorageSourceDto(StorageSource storageSource, StorageSourceAllParamDto storageSourceAllParamDto);

    /**
     * 将请求转换成存储源对象
     */
    StorageSource storageSourceRequestToStorageSource(StorageSourceRequest sourceRequest);
}
