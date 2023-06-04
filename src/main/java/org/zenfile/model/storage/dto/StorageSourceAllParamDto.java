package org.zenfile.model.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 存储源所有参数的汇总
 */
@Data
@ApiModel(value = "存储源配置参数", description = "所有的存储参数汇总")
public class StorageSourceAllParamDto {

    @ApiModelProperty(value = "存储源根路径")
    private String filePath;

    @ApiModelProperty(value = "存储源驱动")
    private String driver;
}
