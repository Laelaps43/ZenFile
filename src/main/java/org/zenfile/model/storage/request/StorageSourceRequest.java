package org.zenfile.model.storage.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zenfile.model.storage.enums.StorageTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 存储源请求类，可能是添加，或者更改
 */
@Data
@ApiModel(description = "为添加或者更改存储源")
public class StorageSourceRequest {

    @ApiModelProperty(value = "需要更新或者新增的的Key", notes = "这个可能是为空，需要根据key来判断，如果不为空，则为更新或者新增的Key")
    private String newKey;
    @ApiModelProperty(value = "存储源Key", example = "key123", notes = "这个为空，则为新添加存储源，不为空更新存储源")
    private String key;

    @ApiModelProperty(value = "存储源名称", example = "OneDriver")
    private String name;

    @ApiModelProperty(value = "存储源类型", example = "LOCAL")
    @NotNull
    private StorageTypeEnum type;

    @ApiModelProperty(value = "存储源根路径")
    @NotNull
    private String filePath;

    @ApiModelProperty(value = "存储源驱动")
    @NotNull
    private String driver;

}
