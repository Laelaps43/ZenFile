package org.zenfile.model.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zenfile.model.file.enums.FileTypeEnum;

/**
 * 文件结果类
 */
@ApiModel(value = "文件结果类")
@Data
public class FileItemResult {

    @ApiModelProperty(value = "文件名")
    private String name;

    @ApiModelProperty(value = "时间")
    private String time;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "类型")
    private FileTypeEnum type;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "下载地址")
    private String url;
}
