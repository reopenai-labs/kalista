package com.reopenai.kalista.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.mybatis.types.JsonField;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Json字符串类型
 *
 * @author Allen Huang
 */
public class JsonFieldTypeHandler extends BaseTypeHandler<JsonField> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int index, JsonField jsonField, JdbcType jdbcType) throws SQLException {
        PGobject object = new PGobject();
        object.setType("json");
        object.setValue(jsonField.getJson());
        preparedStatement.setObject(index, object);
    }

    @Override
    public JsonField getNullableResult(ResultSet resultSet, String name) throws SQLException {
        String json = resultSet.getString(name);
        return StrUtil.isNotBlank(json) ? new JsonField(json) : null;
    }

    @Override
    public JsonField getNullableResult(ResultSet resultSet, int index) throws SQLException {
        String json = resultSet.getString(index);
        return StrUtil.isNotBlank(json) ? new JsonField(json) : null;
    }

    @Override
    public JsonField getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        String json = callableStatement.getString(index);
        return StrUtil.isNotBlank(json) ? new JsonField(json) : null;
    }

}
