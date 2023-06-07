package org.zenfile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zenfile.model.file.entity.FileItem;

@Mapper
public interface FileMapper {
    FileItem getFileItemStorage(@Param("path") String path, @Param("name") String name, @Param("id") Long storageId);

    void insertFileItem(@Param("file") FileItem fileItem);

    void insertFileItemStorageId(@Param("file") FileItem fileItem);
}
