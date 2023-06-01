package org.zenfile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zenfile.exception.storageSource.InitializeStorageSourceException;
import org.zenfile.exception.storageSource.InvalidStorageSourceException;
import org.zenfile.exception.storageSource.StorageSourceException;
import org.zenfile.model.ResultJson;

import java.io.FileNotFoundException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 存储元初始化异常处理
     */
    @ExceptionHandler(InitializeStorageSourceException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> initializeStorageSourceException(StorageSourceException e){
        log.error("存储源 {} 出现异常, 异常：{}", e.getStorageId(),e.getMessage());
        return ResultJson.getError(e.getCodeMsgMessage());
    }

    /**
     * 文件不存在异常处理
     */
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> fileNotFoundException(FileNotFoundException exception){
        log.error(exception.getMessage());
        return ResultJson.getError(exception.getMessage());
    }

    /**
     * 非法的存储源处理
     */
    @ExceptionHandler(InvalidStorageSourceException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> invalidStorageSourceException(InvalidStorageSourceException exception){
        log.error("存储源{}，{}", exception.getStorageId(), exception.getMessage());
        return ResultJson.getError(exception.getCodeMsgMessage());
    }
}