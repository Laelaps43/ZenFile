package org.zenfile.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.zenfile.model.ZenFileEnum;
import org.zenfile.model.file.enums.FileTypeEnum;
import org.zenfile.model.storage.enums.StorageTypeEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * mybatis 的自定义类型转换
 * @param <E>
 */

@MappedTypes({StorageTypeEnum.class, FileTypeEnum.class})
@Slf4j
public class MyBatisZenFileEnumTypeHandler<E extends Enum<E> & ZenFileEnum> extends BaseTypeHandler<E> {

    // 枚举类
    private Class<E> type;
    private E[] enums;

    public MyBatisZenFileEnumTypeHandler(Class<E> type) {
        if(type == null){
            throw new IllegalArgumentException("Type Argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null){
            throw  new IllegalArgumentException(type.getSimpleName());
        }
    }

    // 将传递过来的枚举类型转换成数据库可存储的数据
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        log.debug("将一个枚举类型{}转换成数据库值{}", parameter.getClass(), parameter.getValue());
        if(jdbcType == null){
            ps.setString(i, Objects.toString(parameter.getValue()));
        }else {
            ps.setObject(i, parameter.getValue(), jdbcType.TYPE_CODE);
        }
    }

    // 根据返回的值确定枚举类型
    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if(value != null) {
            return toEnum(value);
        }
        return null;
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if(value != null){
            return toEnum(value);
        }
        return null;
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if(value != null){
            return toEnum(value);
        }
        return null;
    }

    // 根据枚举值来确定枚举类型
    private E toEnum(String value){
        log.debug("将从数据库中查询的{}转换成对应的枚举类型", value);
        for (E e: enums) {
            if((e.getValue()).toString().equals(value)){
                return e;
            }
        }
        throw new IllegalArgumentException("不存在值为"+value + "，请核对");
    }
}
