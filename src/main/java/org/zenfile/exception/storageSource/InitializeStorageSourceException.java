package org.zenfile.exception.storageSource;

import org.zenfile.utils.CodeMsg;

public class InitializeStorageSourceException extends StorageSourceException {


    public InitializeStorageSourceException(CodeMsg codeMsg, Long storageId, String message) {
        super(codeMsg, storageId, message);
    }

    public InitializeStorageSourceException(CodeMsg codeMsg, Long storageId, String message, Throwable cause) {
        super(codeMsg, storageId, message, cause);
    }
}
