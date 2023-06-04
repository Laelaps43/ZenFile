package org.zenfile.convert;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.zenfile.model.storage.dto.StorageSourceAllParamDto;
import org.zenfile.model.storage.dto.StorageSourceDto;
import org.zenfile.model.storage.entity.StorageSource;
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

    StorageSourceDto getStorageSourceDto(StorageSource storageSource, StorageSourceAllParamDto storageSourceAllParamDto);
}
