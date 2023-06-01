package org.zenfile.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统的Json返回封装
 */

@Data
@AllArgsConstructor
@ApiModel(value = "返回结果Json", description = "用于RESTful风格的返回对象")
public class ResultJson<T> implements Serializable {

    @ApiModelProperty(value = "业务状态码，200为正常，其他为异常", example = "200")
    private int code;

    @ApiModelProperty(value = "响应消息", example = "ok")
    private String msg;

    @ApiModelProperty(value = "响应数据")
    private T data;

    /**
     * 定义业务状态码
     */

    // 成功状态码
    public static final  int CODE_SUCCESS = 200;

    // 失败状态码
    public static final  int CODE_ERROR = 500;


    /**
     * 返回成功
     */

    public static ResultJson<Void> getSuccess(){
        return new ResultJson<>(CODE_SUCCESS, "ok", null);
    }

    public static ResultJson<Void> getSuccess(String msg){
        return new ResultJson<>(CODE_SUCCESS, msg, null);
    }

    public static <T> ResultJson<T> getSuccessData(T data){
        return new ResultJson<>(CODE_SUCCESS,"ok", data);
    }

    /**
     * 返回失败
     */

    public static ResultJson<?> getError(){
        return new ResultJson<>(CODE_ERROR, "error", null);
    }

    public static ResultJson<String> getError(String msg){
        return new ResultJson<>(CODE_ERROR, msg, null);
    }


}
