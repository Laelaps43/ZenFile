package org.zenfile.model.storage.entity;

import lombok.Data;

@Data
public class StorageSourceConfig {

    private Long id;

    private Long storageId;

    private String title;

    private String value;
}
