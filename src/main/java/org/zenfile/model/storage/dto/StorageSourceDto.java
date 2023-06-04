package org.zenfile.model.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zenfile.model.storage.enums.StorageTypeEnum;

@ApiModel(value = "存储源返回DTO", description = "包含有存储源信息，以及所有的参数信息")
@Data
public class StorageSourceDto {

    @ApiModelProperty(value = "存储源名称", example = "OneDrive")
    private String name;

    @ApiModelProperty(value = "存储源Key", example = "key123")
    private String key;

    @ApiModelProperty(value = "存储源类型", example = "LOCAL")
    private StorageTypeEnum type;

    @ApiModelProperty(value = "存储源大小", example = "100")
    private Long total;

    @ApiModelProperty(value = "存储源已用大小", example = "20")
    private Long used;

    @ApiModelProperty(value = "存储源根路径")
    private String filePath;

    @ApiModelProperty(value = "存储源驱动")
    private String driver;
}
