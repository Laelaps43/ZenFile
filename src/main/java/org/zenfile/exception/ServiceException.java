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

    public ServiceException(String message, CodeMsg codeMsg) {
        super(message);
        this.codeMsg = codeMsg;
    }

    public ServiceException(String message, Throwable cause, CodeMsg codeMsg) {
        super(message, cause);
        this.codeMsg = codeMsg;
    }

    public ServiceException(Throwable cause, CodeMsg codeMsg) {
        super(cause);
        this.codeMsg = codeMsg;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, CodeMsg codeMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeMsg = codeMsg;
    }
}
