package com.reopenai.kalista.base.structure.page;

import io.protostuff.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.function.Function;

/**
 * 分页查询RPC请求参数结构体
 *
 * @author Allen Huang
 */
@Data
@PageSearchParam.PageSearchChecker
public class PageSearchParam<T> implements Serializable {

    @Tag(1)
    @Min(value = 1, message = "pageNum cannot be less than 1")
    @Schema(
            example = "1",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "页码数"
    )
    private int pageNum;

    @Tag(2)
    @Min(value = 1, message = "pageSize cannot be less than 1")
    @Max(value = 1024, message = "pageSize cannot be more than 1024")
    @Schema(
            example = "1",
            minimum = "1",
            maximum = "1024",
            description = "页大小",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int pageSize;

    @Tag(3)
    @Schema(
            example = "false",
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "是否查询总记录数。查询总记录数会严重影响性能，非必要不查询. "
    )
    private boolean count;

    @Tag(4)
    @Size(max = 10, message = "orders cannot be more than 10")
    @Schema(description = "分页查询的生序排序字段, 最大不能超过10个排序字段")
    private Set<String> ascFields;

    @Tag(5)
    @Size(max = 10, message = "orders cannot be more than 10")
    @Schema(description = "分页查询的降序排序字段, 最大不能超过10个排序字段")
    private Set<String> descFields;

    @Valid
    @Tag(99)
    @NotNull(message = "params cannot be null")
    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "查询的条件参数. 如果你的查询没有任何条件, 请传入一个空的对象(非null)"
    )
    private T params;

    /**
     * 创建一个默认的分页查询请求参数。
     * 默认的请求参数的pageSize=1，pageNum=10
     *
     * @return 分页查询参数实例
     */
    public static <T> PageSearchParam<T> create() {
        return from(10, 1);
    }

    /**
     * 根据页大小和页码数创建分页查询参数实例
     *
     * @param pageSize 页大小
     * @param pageNum  页码
     * @return 分页查询参数实例
     */
    public static <T> PageSearchParam<T> from(int pageSize, int pageNum) {
        return from(pageSize, pageNum, false);
    }

    /**
     * 根据页大小和页码数创建分页查询参数实例
     *
     * @param pageSize 页大小
     * @param pageNum  页码
     * @param count    是否查询总记录数
     * @return 分页查询参数实例
     */
    public static <T> PageSearchParam<T> from(int pageSize, int pageNum, boolean count) {
        PageSearchParam<T> param = new PageSearchParam<>();
        param.setCount(count);
        param.setPageNum(pageNum);
        param.setPageSize(pageSize);
        return param;
    }

    /**
     * 根据分页查询请求参数创建分页查询参数实例
     *
     * @param request 分页查询请求参数
     * @return 分页查询参数实例
     */
    public static <P extends PageSearchRequest, T> PageSearchParam<T> from(P request, Function<P, T> func) {
        PageSearchParam<T> param = new PageSearchParam<>();
        param.setCount(request.isCount());
        param.setPageNum(request.getPageNum());
        param.setPageSize(request.getPageSize());
        param.setAscFields(request.getAscFields());
        param.setDescFields(request.getDescFields());
        param.setParams(func.apply(request));
        return param;
    }

    /**
     * 复制一份分页查询参数
     *
     * @param request   原分页查询请求
     * @param converter 参数转换函数
     * @return 转换后的结果
     */
    public static <T, P> PageSearchParam<P> copy(PageSearchParam<T> request, Function<T, P> converter) {
        PageSearchParam<P> result = new PageSearchParam<>();
        result.setCount(request.isCount());
        result.setAscFields(request.getAscFields());
        result.setDescFields(request.getDescFields());
        result.setPageNum(request.getPageNum());
        result.setPageSize(request.getPageSize());
        T params = request.getParams();
        if (params != null && converter != null) {
            result.setParams(converter.apply(params));
        }
        return result;
    }

    /**
     * 分页查询参数验证器
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = PageSearchChecker.PageSearchCheckerValidator.class)
    @interface PageSearchChecker {

        /**
         * 异常消息
         */
        String message() default "";

        /**
         * 分组
         */
        Class<?>[] groups() default {};

        /**
         * 有效载荷
         */
        Class<? extends Payload>[] payload() default {};


        final class PageSearchCheckerValidator implements ConstraintValidator<PageSearchChecker, PageSearchParam<?>> {

            @Override
            public boolean isValid(PageSearchParam<?> value, ConstraintValidatorContext context) {
                int pageNum = value.getPageNum();
                int pageSize = value.getPageSize();
                int offset = (pageNum - 1) * pageSize;
                if (offset > 100000000) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Paging query exceeds the maximum number of items that can be queried (100000000)")
                            .addConstraintViolation();
                    return false;
                }
                return true;
            }

        }

    }

}
