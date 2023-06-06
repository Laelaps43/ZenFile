package org.zenfile.exception;


import org.zenfile.utils.CodeMsg;

/**
 * 系统异常
 */
public class ZenFileException extends RuntimeException{
    private CodeMsg codeMsg;

    public String getCodeMsgMessage(){
        return codeMsg.getMsg();
    }
    public ZenFileException(CodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }

    public ZenFileException(CodeMsg codeMsg, String message) {
        super(message);
        this.codeMsg = codeMsg;
    }

    public ZenFileException(CodeMsg codeMsg, String message, Throwable cause) {
        super(message, cause);
        this.codeMsg = codeMsg;
    }

    public ZenFileException(CodeMsg codeMsg, Throwable cause) {
        super(cause);
        this.codeMsg = codeMsg;
    }

    public ZenFileException(CodeMsg codeMsg, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.codeMsg = codeMsg;
    }
}
