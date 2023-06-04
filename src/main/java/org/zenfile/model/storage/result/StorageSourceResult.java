package org.zenfile.model.storage.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.zenfile.model.storage.enums.StorageTypeEnum;

@ApiModel(description = "存储源的返回对象")
@Data
public class StorageSourceResult {

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
}
