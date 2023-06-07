package org.zenfile.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zenfile.mapper.FileMapper;
import org.zenfile.model.file.entity.FileItem;
import org.zenfile.utils.RedisStorageUtils;
import org.zenfile.utils.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class FileService {


    @Resource
    RedisStorageUtils redisStorageUtils;

    @Resource
    FileMapper fileMapper;

    /**
     * 根据路径和存储源Id,获取指定的FileItem信息，这里获取的信息可能并不全面
     * 为了不连表查询，这里查询的可能会忽视CreateUser等
     * 同时我们这里没有 [/]这个文件,他不是一个文件，其他的全都是文件
     * @param pathAndName 路径 + 名字
     * @param storageId 存储源Id
     * @return FileItem信息
     */
    public FileItem getFileItemStorage(String pathAndName, Long storageId) {
        FileItem fileItem = redisStorageUtils.getFileItemByPathAndStorage(pathAndName, storageId);
        if(fileItem == null){
            String[] strings = StringUtils.separatePathAndName(pathAndName);
            fileItem = fileMapper.getFileItemStorage(strings[0], strings[1], storageId);
            redisStorageUtils.setFileItemAndStorage(storageId, fileItem);
            log.debug("未从Redis中获取{}信息，从数据空查询，并存储在Redis中", pathAndName);
        }
        log.debug("{}的对象为{}", pathAndName, fileItem);
        return fileItem;
    }


    /**
     * 向数据库中插入数据，因为是新插入的数据，
     * 所有要先获取到插入文件的主键，在去插入另外一张表。
     */
    @Transactional
    public void insertFileItemToDataBase(FileItem fileItem) {
        fileMapper.insertFileItem(fileItem);
        fileMapper.insertFileItemStorageId(fileItem);
    }

    /**
     * 判断给定文件是否存在，存在返回True，不存在返回False
     */
    public boolean isExistFile(FileItem fileItem) {
        return fileMapper.getFileItemStorage(fileItem.getPath(), fileItem.getName(), fileItem.getStorageId()) != null;
    }
}
