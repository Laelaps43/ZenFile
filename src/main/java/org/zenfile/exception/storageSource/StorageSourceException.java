package org.zenfile.exception.storageSource;

import lombok.Getter;
import org.zenfile.exception.ServiceException;
import org.zenfile.utils.CodeMsg;

/**
 * 存储源异常处理类
 */
@Getter
public class StorageSourceException extends ServiceException {

    /**
     * 存储元Id
     */
    private final Long storageId;


    public StorageSourceException(CodeMsg codeMsg, Long storageId, String message) {
        super(codeMsg, message);
        this.storageId = storageId;
    }

    public StorageSourceException(CodeMsg codeMsg, Long storageId, String message, Throwable cause){
        super(codeMsg, cause, message);
        this.storageId = storageId;
    }
}
