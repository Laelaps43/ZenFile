package org.zenfile.service.storage.base;


import org.zenfile.model.file.entity.FileItem;
import org.zenfile.model.storage.enums.StorageTypeEnum;

import java.util.List;

/**
 * 最基础的文件接口
 */

public interface BaseFileService {

    /**
     * 获取指定文件夹下的文件和文件夹, 从数据库中获取
     * @param folderPath 给定的文件夹路径
     */
    List<FileItem> fileList(String folderPath) throws Exception;

    /**
     *  获取给定文件的下载地址
     * @param pathAndName 文件路径+文件名
     * @return 下载地址
     */
    String getDownloadUrl(String pathAndName);


    /**
     * 获取存储源类型
     *
     * @return 存储元类型
     */
    StorageTypeEnum getStorageTypeEnum();

}
