package org.zenfile.exception;

import lombok.Data;
import org.zenfile.utils.CodeMsg;

/**
 * 系统Service层错误信息
 * message信息为日志打出
 * CodeMsg为返回给用户端
 */
@Data
public class ServiceException extends RuntimeException{

    private CodeMsg codeMsg;

    public String getCodeMsgMessage(){
        return codeMsg.getMsg();
    }
    public ServiceException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public ServiceException( CodeMsg codeMsg, String message) {
        super(message);
        this.codeMsg = codeMsg;
    }

    public ServiceException(CodeMsg codeMsg,  Throwable cause, String message) {
        super(message, cause);
        this.codeMsg = codeMsg;
    }

    public ServiceException(CodeMsg codeMsg, Throwable cause) {
        super(cause);
        this.codeMsg = codeMsg;
    }

    public ServiceException(CodeMsg codeMsg, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeMsg = codeMsg;
    }
}
