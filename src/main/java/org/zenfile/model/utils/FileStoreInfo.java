package org.zenfile.model.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 设备硬盘信息
 */
@Data
@AllArgsConstructor
public class FileStoreInfo {
    private String name;

    private String volumeName;

    private Long totalSpace;

    private Long freeSpace;
}
