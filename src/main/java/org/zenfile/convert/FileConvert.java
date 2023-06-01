package org.zenfile.convert;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.zenfile.model.file.dto.FileItemResult;
import org.zenfile.model.file.entity.FileItem;

/**
 * 文件转换类，用来关于文件的model类相互转换
 */
@Component
@Mapper(componentModel = "spring")
public interface FileConvert {

    /**
     * 将 FileItem 转换为 FileItemResult
     */
    FileItemResult fileItemToResult(FileItem fileItem);
}
