package org.zenfile.model.storage.entity;

import lombok.Data;
import org.zenfile.model.storage.enums.StorageTypeEnum;

import java.util.Date;

/**
 * 存储源对象，对应数据库中的storage_source对象
 */
@Data
public class StorageSource {

    private Long id;

    private String name;

    private String key;

    private StorageTypeEnum type;

    private Date createTime;

    private Long createUser;

}
