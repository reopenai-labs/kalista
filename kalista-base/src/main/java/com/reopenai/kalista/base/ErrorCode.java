package com.reopenai.kalista.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Allen Huang
 */
public interface ErrorCode {

    String getValue();

    /**
     * 内置的一些错误码
     */
    @Getter
    @RequiredArgsConstructor
    enum Builtin implements ErrorCode {

        SUCCESS("200"),
        //---------------
        //  HTTP请求相关
        //---------------
        /**
         * 请求错误
         */
        BAD_REQUEST("400"),
        /**
         * 未认证
         */
        UNAUTHORIZED("401"),
        /**
         * 没有权限
         */
        FORBIDDEN("403"),
        /**
         * 资源不存在
         */
        NOT_FOUND("404"),
        /**
         * 请求方法错误
         */
        METHOD_NOT_ALLOWED("405"),
        /**
         * 无法解析ACCEPT
         */
        NOT_ACCEPTABLE("406"),
        /**
         * 请求的Media type错误
         */
        MEDIA_TYPE_NOT_ALLOWED("415"),
        /**
         * 请求太多被熔断
         */
        MANY_REQUEST("429"),
        /**
         * 未知异常
         */
        SERVER_ERROR("500"),
        /**
         * 未通过参数检查
         */
        VALIDATION_FAILED("4001"),
        /**
         * 请求参数丢失
         */
        MISSING_REQUEST_PARAMETER("4002"),
        /**
         * 请求Token验证失败
         */
        INVALID_ACCESS_TOKEN("4003"),
        /**
         * 请求签名验证失败
         */
        INVALID_ACCESS_SIGNATURE("4004"),
        /**
         * 非法的参数值
         */
        INVALID_PARAMETER("4006"),

        /**
         * 请求参数类型不匹配
         */
        PARAM_TYPE_MISMATCH("4008"),

        //---------------
        //  内部错误
        //---------------
        DISTRIBUTED_LOCK_ACQUIRE_LOCK_TIMEOUT("5001"),

        //---------------
        //  RPC相关
        //---------------
        /**
         * RPC接口参数错误
         */
        RPC_INVALID_ARGUMENT("5100"),
        /**
         * RPC接口不存在
         */
        RPC_NOT_FOUND("5101"),
        /**
         * RPC接口被限流
         */
        PRC_RESOURCE_EXHAUSTED("5102"),
        /**
         * RPC服务端执行异常
         */
        RPC_SERVER_ERROR("5103"),
        /**
         * RPC timeout异常
         */
        RPC_TIMEOUT("5104"),
        /**
         * RPC的目标对象状态不正常
         */
        RPC_UNAVAILABLE("5105"),

        //---------------
        //  DB相关
        //---------------
        DB_UNKNOWN_ERROR("5201"),

        ;

        private final String value;

    }


    /**
     * 创建一个临时的错误码实例
     *
     * @param code 错误码
     * @return 错误码实例
     */
    static Temporary temporary(String code) {
        return new Temporary(code);
    }

    record Temporary(String code) implements ErrorCode {
        @Override
        public String getValue() {
            return this.code;
        }
    }

}
