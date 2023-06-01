package org.zenfile.model.storage.param;

import lombok.Getter;

@Getter
public class DriveTypeParam extends ProxyTransferParam{

    // 存储驱动类型, 本地为分区,oneDriver 为oneDriver
    private String driver;
}
