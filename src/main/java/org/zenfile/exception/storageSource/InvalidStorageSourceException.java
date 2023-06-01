package org.zenfile.exception.storageSource;

import org.zenfile.utils.CodeMsg;

public class InvalidStorageSourceException extends StorageSourceException{

    public InvalidStorageSourceException(CodeMsg codeMsg, Long storageId, String message) {
        super(codeMsg, storageId, message);
    }

    public InvalidStorageSourceException(CodeMsg codeMsg, Long storageId, String message, Throwable cause) {
        super(codeMsg, storageId, message, cause);
    }

    public InvalidStorageSourceException(Long storageId){
        super(CodeMsg.NOTFOUND_STORAGE_SOURCE, storageId, CodeMsg.NOTFOUND_STORAGE_SOURCE.getMsg());
    }
}
