package com.reopenai.kalista.mybatis.handler;

import com.reopenai.kalista.mybatis.types.NumberArray;
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
public class NumberArrayTypeHandler extends BaseTypeHandler<NumberArray> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, NumberArray values, JdbcType jdbcType) throws SQLException {
        Number[] array = values.toArray(new Number[0]);
        Array arr = preparedStatement.getConnection().createArrayOf(JdbcType.BIGINT.name(), array);
        preparedStatement.setObject(i, arr);
    }

    @Override
    public NumberArray getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Array array = resultSet.getArray(s);
        if (array != null) {
            Number[] elements = (Number[]) array.getArray();
            return NumberArray.from(elements);
        }
        return null;
    }

    @Override
    public NumberArray getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Array array = resultSet.getArray(i);
        if (array != null) {
            Number[] elements = (Number[]) array.getArray();
            return NumberArray.from(elements);
        }
        return null;
    }

    @Override
    public NumberArray getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        Array array = callableStatement.getArray(i);
        if (array != null) {
            Number[] elements = (Number[]) array.getArray();
            return NumberArray.from(elements);
        }
        return null;
    }

}
