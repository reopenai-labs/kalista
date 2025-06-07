package com.reopenai.kalista.mybatis.helper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reopenai.kalista.base.structure.page.PageSearchParam;
import com.reopenai.kalista.base.structure.page.PageSearchResp;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于MyBatis Plus的分页查询工具。
 *
 * @author Allen Huang
 * @see PageSearchParam
 * @see PageSearchResp
 */
public class PageSearchHelper {

    /**
     * 根据总记录数、页大小计算总页数
     *
     * @param total   总记录数
     * @param maxSize 页面的条目数
     * @return 总页数
     */
    public static long totalPage(long total, int maxSize) {
        return (total + maxSize - 1) / maxSize;
    }

    /**
     * 根据分页查询参数构建MyBatis-Plus的分页查询对象。
     * 如果查询参数中存在排序参数，此方法会尝试将排序字段的名称从驼峰转换成下划线格式。
     * 如果排序参数中未指定排序规则，默认会采用升序的排序规则。
     *
     * @param request 分页查询参数
     * @return MyBatis-Plus分页查询对象
     */
    public static <R> Page<R> createPage(PageSearchParam<?> request) {
        Page<R> page = new Page<>(request.getPageNum(), request.getPageSize());
        page.setSearchCount(request.isCount());
        Set<String> ascFields = request.getAscFields();
        if (ascFields != null && !ascFields.isEmpty()) {
            for (String ascField : ascFields) {
                ascField = StrUtil.toUnderlineCase(ascField);
                page.addOrder(OrderItem.asc(ascField));
            }
        }
        Set<String> descFields = request.getDescFields();
        if (descFields != null && !descFields.isEmpty()) {
            for (String descField : descFields) {
                descField = StrUtil.toUnderlineCase(descField);
                page.addOrder(OrderItem.desc(descField));
            }
        }
        return page;
    }

    /**
     * 将MyBatis-Plus的分页查询结果转换成{@link PageSearchResp}
     *
     * @param page MyBatis Plus分页查询结果
     * @return 分页查询结果
     */
    public static <T> PageSearchResp<T> searchResult(IPage<T> page) {
        return searchResult(page, Function.identity());
    }

    /**
     * 将MyBatis-Plus的分页查询结果转换成{@link PageSearchResp}
     *
     * @param page     MyBatis Plus分页查询结果
     * @param function DTO转换函数，将MyBatis-Plus的查询结果转换成想要的实体
     * @return 分页查询结果
     */
    public static <T, R> PageSearchResp<R> searchResult(IPage<T> page, Function<T, R> function) {
        PageSearchResp<R> result = new PageSearchResp<>();
        result.setTotal(page.getSize());
        result.setTotal(page.getTotal());
        List<R> data = Optional.ofNullable(page.getRecords())
                .map(Collection::stream)
                .map(stream -> stream.map(function))
                .map(stream -> stream.collect(Collectors.toList()))
                .orElse(null);
        result.setRecords(data);
        return result;
    }

    /**
     * 快速的分页查询方法
     *
     * @param request  分页查询请求参数
     * @param function 实际执行分页查询的函数
     * @return 分页查询结果
     */
    public static <T, R> PageSearchResp<R> fastSearch(PageSearchParam<T> request, BiFunction<Page<R>, T, IPage<R>> function) {
        Page<R> page = createPage(request);
        return searchResult(function.apply(page, request.getParams()));
    }

    /**
     * 快速的分页查询方法
     *
     * @param request  分页查询请求参数
     * @param function 实际执行分页查询的函数
     * @param trans    查询结果的DTO转换函数
     * @return 分页查询结果
     */
    public static <T, E, R> PageSearchResp<R> fastSearch(PageSearchParam<T> request, BiFunction<Page<E>, T, IPage<E>> function, Function<E, R> trans) {
        Page<E> page = createPage(request);
        return searchResult(function.apply(page, request.getParams()), trans);
    }

    /**
     * 使用分页查询助手开启一个分页查询
     *
     * @return 分页查询结果
     * @see Searcher
     */
    public static <T, E, R> Searcher<T, E, R> startSearch() {
        return new Searcher<>();
    }

    /**
     * 一个分页查询助手，降低查询的复杂度。用例如下:
     * <pre>{@code
     * PageSearchHelper.<DemoRequest, Demo, DemoVo>startSearch()
     *                 .request(searchParam)
     *                 .method(demoReadRepository::doSearch)
     *                 .trans(DemoVO::from)
     *                 .search();
     * }</pre>
     * 如果不需要做DTO转换，你可以调整查询的方法:
     * <pre>{@code
     * PageSearchHelper.<DemoRequest, Demo, DemoVo>startSearch()
     *                 .request(searchParam)
     *                 .method(demoReadRepository::doSearch)
     *                 .trans(Function.identity())
     *                 .search();
     * }</pre>
     */
    public static class Searcher<T, E, R> {

        private PageSearchParam<T> request;

        private BiFunction<Page<E>, T, IPage<E>> function;

        private Function<E, R> trans;

        /**
         * 设计分页查询请求参数
         *
         * @param request 请求参数实例
         * @return Searcher实例
         */
        public Searcher<T, E, R> request(PageSearchParam<T> request) {
            this.request = request;
            return this;
        }

        /**
         * 设置调用分页查询的dao方法的函数
         *
         * @param function 执行实际查询的函数
         * @return Searcher实例
         */
        public Searcher<T, E, R> method(BiFunction<Page<E>, T, IPage<E>> function) {
            this.function = function;
            return this;
        }

        /**
         * 从数据库查询到结果时做结果转换的函数
         *
         * @param trans DTO转换函数
         * @return Searcher实例
         */
        public Searcher<T, E, R> trans(Function<E, R> trans) {
            this.trans = trans;
            return this;
        }

        /**
         * 执行分页查询，并将查询的结果转换成{@link PageSearchResp}
         *
         * @return 分页查询结果
         */
        public PageSearchResp<R> search() {
            if (request == null) {
                throw new IllegalArgumentException("search request can not be null");
            }
            if (function == null) {
                throw new IllegalArgumentException("search method can not be null");
            }
            if (trans == null) {
                throw new IllegalArgumentException("trans can not be null");
            }
            return fastSearch(request, function, trans);
        }
    }


}
