package org.zenfile.model.file.enums;


import org.zenfile.model.ZenFileEnum;

public enum FileTypeEnum implements ZenFileEnum<FileTypeEnum, String> {

    FILE("1"), FOLDER("2");

    private final String type;

    FileTypeEnum(String type) {
        this.type = type;
    }

    @Override
    public String getValue(){
        return this.type;
    }
}
