package org.zenfile.exception.storageSource;


import org.zenfile.utils.CodeMsg;

public class DuplicateDriverException extends StorageSourceException {
    public DuplicateDriverException(CodeMsg codeMsg, Long storageId, String message) {
        super(codeMsg, storageId, message);
    }

    public DuplicateDriverException(CodeMsg codeMsg, Long storageId, String message, Throwable cause) {
        super(codeMsg, storageId, message, cause);
    }

    public DuplicateDriverException(CodeMsg codeMsg, Long storageId){
        super(codeMsg, storageId, codeMsg.getMsg());
    }
}
