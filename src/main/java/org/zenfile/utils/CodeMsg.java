package org.zenfile.utils;

import lombok.Getter;

/**
 * 错误消息类，用来表明系统发生的错误，并返回给用户
 */
@Getter
public class CodeMsg {


    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */

    private String msg;

    public CodeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 存储源异常 50xxx
     */


    public static CodeMsg INITIALIZE_STORAGE_SOURCE = new CodeMsg("50000", "存储源初始化异常");
    public static CodeMsg NOTFOUND_STORAGE_SOURCE = new CodeMsg("50001", "未找到存储元");
}
