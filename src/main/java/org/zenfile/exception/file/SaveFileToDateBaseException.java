package org.zenfile.exception.file;

/**
 * 保存到数据库异常
 */
public class SaveFileToDateBaseException extends FileException{

    public SaveFileToDateBaseException(String message){
        super(null, null, message);
    }
    public SaveFileToDateBaseException(String path, String name) {
        super(path, name);
    }

    public SaveFileToDateBaseException(String path, String name, String message) {
        super(path, name, message);
    }

    public SaveFileToDateBaseException(String path, String name, String message, Throwable cause) {
        super(path, name, message, cause);
    }
}
