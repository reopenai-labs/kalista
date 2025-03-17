package com.reopenai.kalista.mybatis.helper;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * MyBatisContextHelper
 *
 * @author Allen Huang
 */
public final class MyBatisContextHelper {

    static SqlSessionTemplate sqlSessionTemplate;

    /**
     * 创建一个Array类型的数据
     *
     * @param typeName 类型的名称
     * @param elements 数据列表
     * @return Array数据实例
     * @throws SQLException 创建Array失败时抛出的异常
     */
    public static Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        try (SqlSession sqlSession = getSqlSession()) {
            return sqlSession.getConnection().createArrayOf(typeName, elements);
        }
    }

    /**
     * 批量插入
     *
     * @param mapper   mapper对象
     * @param elements 待插入的实例列表
     */
    public static <T> List<BatchResult> executeBatchInsert(Class<? extends BaseMapper<?>> mapper, Collection<T> elements) {
        MybatisBatch<T> mybatisBatch = newMybatisBatch(elements);
        MybatisBatch.Method<T> method = new MybatisBatch.Method<>(mapper);
        return mybatisBatch.execute(method.insert());
    }

    /**
     * 批量通过Id修改数据
     *
     * @param mapper   mapper对象
     * @param elements 待插入的实例列表
     */
    public static <T> List<BatchResult> executeBatchUpdateById(Class<? extends BaseMapper<?>> mapper, Collection<T> elements) {
        MybatisBatch<T> mybatisBatch = newMybatisBatch(elements);
        MybatisBatch.Method<T> method = new MybatisBatch.Method<>(mapper);
        return mybatisBatch.execute(method.updateById());
    }

    /**
     * 获取当前的SqlSession实例
     *
     * @return SqlSession实例
     */
    public static SqlSession getSqlSession() {
        SqlSessionFactory sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
        ExecutorType executorType = sqlSessionTemplate.getExecutorType();
        PersistenceExceptionTranslator persistenceExceptionTranslator = sqlSessionTemplate.getPersistenceExceptionTranslator();
        return SqlSessionUtils.getSqlSession(sqlSessionFactory, executorType, persistenceExceptionTranslator);
    }

    /**
     * 创建MybatisBatch实例
     *
     * @param elements 批对象列表
     * @return MybatisBatch实例
     */
    public static <T> MybatisBatch<T> newMybatisBatch(Collection<T> elements) {
        return new MybatisBatch<>(sqlSessionTemplate.getSqlSessionFactory(), elements);
    }

}
