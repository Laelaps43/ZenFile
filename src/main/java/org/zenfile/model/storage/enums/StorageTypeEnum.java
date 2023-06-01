package org.zenfile.model.storage.enums;

import org.zenfile.model.ZenFileEnum;

public enum StorageTypeEnum implements ZenFileEnum<StorageTypeEnum, String> {

    LOCAL("local"), ONEDRIVE("onedrive");

    private final String type;

    StorageTypeEnum(String type) {
        this.type = type;
    }


    @Override
    public String getValue() {
        return this.type;
    }
}
