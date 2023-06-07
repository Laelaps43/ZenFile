package org.zenfile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zenfile.exception.file.SaveFileToDateBaseException;
import org.zenfile.exception.storageSource.*;
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

    /**
     * 多个重复分区
     */
    @ExceptionHandler(DuplicateDriverException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> duplicateDriverException(DuplicateDriverException exception){
        log.error("系统存在多个重复的分区，请检查! 存储源{}的位置不唯一。", exception.getStorageId());
        return ResultJson.getError(exception.getCodeMsgMessage());
    }

    /**
     * 非法的Key异常处理
     */
    @ExceptionHandler(InvalidStorageSourceKeyException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> invalidStorageSourceKey(InvalidStorageSourceKeyException exception){
        log.error("存储源Key异常：{}", exception.getMessage());
        return ResultJson.getError(exception.getCodeMsgMessage());
    }

    /**
     * 未知操作异常处理
     */
    @ExceptionHandler(UnKnownOperationException.class)
    @ResponseStatus
    @ResponseBody
    public ResultJson<String> unKnowOperationException(UnKnownOperationException exception){
        log.error("未知异常操作");
        return ResultJson.getError(exception.getCodeMsgMessage());
    }

    /**
     * 处理保存到数据库异常
     */
    @ExceptionHandler(SaveFileToDateBaseException.class)
    public void saveFileTODataBaseException(SaveFileToDateBaseException exception){
        log.error("保存到数据库异常：文件{}/{}；异常消息：{}", exception.getName(), exception.getPath(),exception.getMessage());
    }
}
