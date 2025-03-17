package com.reopenai.kalista.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.reopenai.kalista.core.serialization.jackson.JsonUtil;
import com.reopenai.kalista.mybatis.types.VarcharMap;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * varchar[] 和 string[]的相互转换
 *
 * @author Allen Huang
 */
@RequiredArgsConstructor
public class VarcharMapTypeHandler extends BaseTypeHandler<VarcharMap> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int index, VarcharMap values, JdbcType jdbcType) throws SQLException {
        PGobject object = new PGobject();
        object.setType("json");
        object.setValue(JsonUtil.toJSONString(values));
        preparedStatement.setObject(index, object);
    }

    @Override
    public VarcharMap getNullableResult(ResultSet resultSet, String name) throws SQLException {
        String json = resultSet.getString(name);
        return StrUtil.isNotBlank(json) ? parseValue(json) : null;
    }

    @Override
    public VarcharMap getNullableResult(ResultSet resultSet, int index) throws SQLException {
        String json = resultSet.getString(index);
        return StrUtil.isNotBlank(json) ? parseValue(json) : null;
    }

    @Override
    public VarcharMap getNullableResult(CallableStatement callableStatement, int index) throws SQLException {
        String json = callableStatement.getString(index);
        return StrUtil.isNotBlank(json) ? parseValue(json) : null;
    }

    private VarcharMap parseValue(String value) {
        return JsonUtil.parseObject(value, VarcharMap.class);
    }

}
