package org.zenfile.model.file.entity;

import lombok.Data;
import org.zenfile.model.file.enums.FileTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应数据库中的File、fileCreateUser、fileHold、fileStorageSource表
 */
@Data
public class FileItem implements Serializable {

    private Long id;

    private String name;

    private String path;

    private FileTypeEnum type;

    private Long size;

    private Date createTime;

    private Long crateUserId;

    private Long holdId;

    private Long storageId;
}
