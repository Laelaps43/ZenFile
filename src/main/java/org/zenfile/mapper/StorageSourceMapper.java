package org.zenfile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zenfile.model.storage.entity.StorageSource;

import java.util.List;

@Mapper
public interface StorageSourceMapper {


    StorageSource getStorageSourceById(@Param("id") Long storageId);

    List<StorageSource> getAllStorageSource();

    StorageSource getStorageSourceByKey(@Param("key") String key);

    void insertStorageSource(@Param("source") StorageSource storageSource);

    void updateStorageSourceById(@Param("source") StorageSource storageSource);
}
