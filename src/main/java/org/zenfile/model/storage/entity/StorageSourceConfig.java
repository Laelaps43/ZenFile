package org.zenfile.model.storage.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StorageSourceConfig {

    private Long id;

    private Long storageId;

    private String title;

    private String value;

    public StorageSourceConfig() {
    }
}
