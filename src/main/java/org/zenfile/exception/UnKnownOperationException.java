package org.zenfile.exception;

import org.zenfile.utils.CodeMsg;

public class UnKnownOperationException extends ZenFileException {
    public UnKnownOperationException(CodeMsg codeMsg) {
        super(codeMsg);
    }

    public UnKnownOperationException(CodeMsg codeMsg, String message) {
        super(codeMsg, message);
    }
}
