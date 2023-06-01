package org.zenfile.exception.storageSource;

import lombok.Data;
import lombok.Getter;
import org.zenfile.exception.ServiceException;
import org.zenfile.utils.CodeMsg;

import javax.sql.rowset.serial.SerialException;

/**
 * 存储源异常处理类
 */
@Getter
public class StorageSourceException extends ServiceException {

    /**
     * 存储元Id
     */
    private Long storageId;


    public StorageSourceException(CodeMsg codeMsg, Long storageId, String message) {
        super(message, codeMsg);
        this.storageId = storageId;
    }

    public StorageSourceException(CodeMsg codeMsg, Long storageId, String message, Throwable cause){
        super(message, cause, codeMsg);
        this.storageId = storageId;
    }
}
