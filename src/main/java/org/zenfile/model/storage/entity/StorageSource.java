package org.zenfile.model.storage.entity;

import lombok.Data;
import org.zenfile.model.storage.enums.StorageTypeEnum;

import java.util.Date;

/**
 * 存储源对象，对应数据库中的storage_source对象
 */
@Data
public class StorageSource {

    /**
     * 主键
     */
    private Long id;

    /**
     * 存储名称
     */
    private String name;

    /**
     * 存储源名称
     */
    private String key;

    /**
     * 存储源类型
     */
    private StorageTypeEnum type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建userId
     */
    private Long createUser;

}
