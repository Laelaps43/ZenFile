package org.zenfile.exception.file;

import lombok.Getter;
import org.zenfile.exception.ZenFileException;

/**
 * 文件异常类
 */
@Getter
public class FileException extends ZenFileException {

    private final String path;
    private final String name;

    public FileException(String path, String name) {
        super(null);
        this.path = path;
        this.name = name;
    }

    public FileException(String path, String name, String message) {
        super(null, message);
        this.path = path;
        this.name = name;
    }

    public FileException(String path, String name, String message, Throwable cause) {
        super(null, message, cause);
        this.path = path;
        this.name = name;
    }

    public FileException(String path, String name, Throwable cause) {
        super(null, cause);
        this.path = path;
        this.name = name;
    }

    public FileException(String path, String name, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(null, message, cause, enableSuppression, writableStackTrace);
        this.path = path;
        this.name = name;
    }
}
