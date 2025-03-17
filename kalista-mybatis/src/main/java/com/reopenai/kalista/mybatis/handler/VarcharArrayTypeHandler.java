package com.reopenai.kalista.mybatis.handler;

import com.reopenai.kalista.mybatis.types.VarcharArray;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * varchar[] 和 string[]的相互转换
 *
 * @author Allen Huang
 */
@RequiredArgsConstructor
public class VarcharArrayTypeHandler extends BaseTypeHandler<VarcharArray> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, VarcharArray values, JdbcType jdbcType) throws SQLException {
        String[] array = values.toArray(new String[0]);
        Array arr = preparedStatement.getConnection().createArrayOf(JdbcType.VARCHAR.name(), array);
        preparedStatement.setObject(i, arr);
    }

    @Override
    public VarcharArray getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Array array = resultSet.getArray(s);
        if (array != null) {
            String[] elements = (String[]) array.getArray();
            return VarcharArray.from(elements);
        }
        return null;
    }

    @Override
    public VarcharArray getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Array array = resultSet.getArray(i);
        if (array != null) {
            String[] elements = (String[]) array.getArray();
            return VarcharArray.from(elements);
        }
        return null;
    }

    @Override
    public VarcharArray getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Array array = callableStatement.getArray(i);
        if (array != null) {
            String[] elements = (String[]) array.getArray();
            return VarcharArray.from(elements);
        }
        return null;
    }

}
