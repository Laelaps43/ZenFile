package org.zenfile.exception.storageSource;

import org.zenfile.utils.CodeMsg;

public class InvalidStorageSourceKeyException extends StorageSourceException{
    public InvalidStorageSourceKeyException(CodeMsg codeMsg, Long storageId, String message) {
        super(codeMsg, storageId, message);
    }

    public InvalidStorageSourceKeyException(CodeMsg codeMsg, Long storageId, String message, Throwable cause) {
        super(codeMsg, storageId, message, cause);
    }
}
