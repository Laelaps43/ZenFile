package org.zenfile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zenfile.model.storage.entity.StorageSourceConfig;

import java.util.List;

@Mapper
public interface StorageSourceConfigMapper {
    List<StorageSourceConfig> getStorageSourceConfigByStorageId(@Param("storageId") Long storageSourceId);
}
