package com.reopenai.kalista.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Locale对象和数据库字段的类型处理器ø
 *
 * @author Allen Huang
 */
@MappedTypes(Locale.class)
public class LocaleTypeHandler extends BaseTypeHandler<Locale> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int index, Locale locale, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(index, locale.toLanguageTag());
    }

    @Override
    public Locale getNullableResult(ResultSet resultSet, String column) throws SQLException {
        return Locale.forLanguageTag(resultSet.getString(column));
    }

    @Override
    public Locale getNullableResult(ResultSet resultSet, int index) throws SQLException {
        return Locale.forLanguageTag(resultSet.getString(index));
    }

    @Override
    public Locale getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        return Locale.forLanguageTag(callableStatement.getString(index));
    }

}
