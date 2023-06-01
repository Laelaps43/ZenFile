package org.zenfile.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemConfigMapper {
    String getSystemConfigByTitle(@Param("title") String title);
}
