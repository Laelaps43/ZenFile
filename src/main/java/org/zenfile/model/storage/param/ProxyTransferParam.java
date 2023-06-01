package org.zenfile.model.storage.param;

import lombok.Getter;

@Getter
public class ProxyTransferParam implements StorageParam{

    // 是否对链接开启签名
    private boolean isPrivate;
}
