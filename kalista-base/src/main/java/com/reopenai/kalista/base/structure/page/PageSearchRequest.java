package com.reopenai.kalista.base.structure.page;

import io.protostuff.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

/**
 * 分页查询API请求参数结构体
 *
 * @author Allen Huang
 */
@Data
@PageSearchRequest.PageSearchChecker
public class PageSearchRequest implements Serializable {

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

    /**
     * 复制一份分页查询参数
     */
    public static <T extends PageSearchRequest, R extends PageSearchRequest> void copy(T source, R target) {
        target.setCount(source.isCount());
        target.setAscFields(source.getAscFields());
        target.setDescFields(source.getDescFields());
        target.setPageNum(source.getPageNum());
        target.setPageSize(source.getPageSize());
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


        final class PageSearchCheckerValidator implements ConstraintValidator<PageSearchChecker, PageSearchRequest> {

            @Override
            public boolean isValid(PageSearchRequest value, ConstraintValidatorContext context) {
                int pageSize = value.getPageSize();
                int pageNum = value.getPageNum();
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
