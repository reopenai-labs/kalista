package com.reopenai.kalista.mybatis.handler;

import com.reopenai.kalista.base.enums.XEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举处理器，数据库采用非枚举名称时的处理器。
 *
 * @author Allen Huang
 */
@SuppressWarnings("rawtypes")
public class XEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Map<Object, E> valueMap;

    private final Class<?> valueType;

    public XEnumTypeHandler(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        Map<Object, E> data = new HashMap<>(enumConstants.length);
        for (E e : enumConstants) {
            data.put(((XEnum) e).getValue(), e);
        }
        this.valueMap = data;
        this.valueType = parseValueType(enumClass);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, ((XEnum) parameter).getValue());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object result = rs.getObject(columnName, valueType);
        return valueMap.get(result);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object result = rs.getObject(columnIndex, valueType);
        return valueMap.get(result);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object result = cs.getObject(columnIndex, valueType);
        return valueMap.get(result);
    }

    private Class<?> parseValueType(Class<E> enumClass) {
        Type[] genericInterfaces = enumClass.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
            if (parameterizedType.getRawType() == XEnum.class) {
                Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
                return (Class<?>) actualTypeArgument;
            }
        }
        return Object.class;
    }

    /**
     * 该枚举是否实现了{@link XEnum}
     *
     * @param type 枚举的类型
     * @return 如果匹配成功则返回true
     */
    public static boolean isMatch(Class<?> type) {
        return type != null && type.isEnum() && XEnum.class.isAssignableFrom(type);
    }

}
