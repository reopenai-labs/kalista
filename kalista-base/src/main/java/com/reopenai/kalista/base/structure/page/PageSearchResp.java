package com.reopenai.kalista.base.structure.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 分页查询响应的结构体
 *
 * @author Allen Huang
 */
@Data
@Schema(description = "分页查询结果统计结构体")
public class PageSearchResp<E> implements Serializable {

    @Schema(description = "总记录数。如果请求参数中'是否查询总记录数'为true则此参数将会返回.")
    private Long total;

    @Schema(description = "分页查询的结果", requiredMode = Schema.RequiredMode.REQUIRED)
    private Collection<E> result;

    @Schema(description = "扩展字段.如果存在OrderData字段，在查询下一页时需要将此字段带入extra中.")
    private Map<String, Object> extra;

    /**
     * 是否含有OrderData字段
     *
     * @return OrderData字段
     */
    public boolean hasOrderData() {
        return getOrderData() != null;
    }

    /**
     * 获取扩展属性中的orderData数据
     *
     * @return 如果存在则返回，否则将返回Null
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <RESPONSE> RESPONSE getOrderData() {
        return (RESPONSE) Optional.ofNullable(extra)
                .map(data -> data.get("orderData"))
                .orElse(null);
    }

    /**
     * 重新设置OrderData
     *
     * @param data OrderData的数据
     */
    @JsonIgnore
    public <DATA> void resetOrderData(DATA data) {
        Map<String, Object> map = this.extra == null ? new HashMap<>(2) : new HashMap<>(this.extra);
        map.put("orderData", data);
        this.extra = map;
    }

    /**
     * 转换分页查询结果的泛型
     *
     * @param result 要返回的数据列表
     * @return 分页查询结果实例
     */
    public <R> PageSearchResp<R> create(Collection<R> result) {
        PageSearchResp<R> searchResult = new PageSearchResp<>();
        searchResult.setResult(result);
        searchResult.setTotal(this.getTotal());
        searchResult.setExtra(new HashMap<>(2));
        return searchResult;
    }

    /**
     * 转换分页查询结果的泛型
     *
     * @param pageResult 原始结果
     * @param result     新的返回结果内容
     * @return 转换后的分页查询结果
     */
    public static <T, R> PageSearchResp<R> transform(PageSearchResp<T> pageResult, Collection<R> result) {
        PageSearchResp<R> searchResult = new PageSearchResp<>();
        searchResult.setResult(result);
        searchResult.setTotal(pageResult.getTotal());
        searchResult.setExtra(pageResult.getExtra());
        return searchResult;
    }

    /**
     * 创建分页查询结果实例。
     *
     * @param data  分页查询返回的数据内容
     * @param total 分页查询的总记录数
     * @return 分页查询结果实例
     */
    public static <T> PageSearchResp<T> create(Collection<T> data, Long total) {
        PageSearchResp<T> result = new PageSearchResp<>();
        result.setTotal(total);
        result.setResult(data);
        result.setExtra(new HashMap<>(2));
        return result;
    }

    /**
     * 创建一个不包含任何返回数据(null)的分页查询结果
     *
     * @return 分页查询结果实例
     */
    public static <T> PageSearchResp<T> emptyOfNull() {
        PageSearchResp<T> result = new PageSearchResp<>();
        result.setTotal(0L);
        result.setExtra(new HashMap<>(2));
        return result;
    }

    /**
     * 创建一个空的列表的分页查询结果
     *
     * @return 分页查询结果实例
     */
    public static <T> PageSearchResp<T> emptyOfArray() {
        PageSearchResp<T> result = new PageSearchResp<>();
        result.setTotal(0L);
        result.setResult(Collections.emptyList());
        return result;
    }


}
