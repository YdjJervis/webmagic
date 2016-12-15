package com.eccang;

/**
 * 常量定义类
 */
public final class R {

    /** 客户-ASIN 关系的设置 */
    public static final class AsinSetting {
        /** 更新频率爬取 */
        public static final int UPDATE_FREQUENCY = 24;
    }

    /** 请求状态 */
    public static final class HttpStatus {
        /** 请求成功 */
        public static final int SUCCESS = 200;
        /** 授权失败 */
        public static final int AUTH_FAILURE = 401;
        /** 参数异常 */
        public static final int PARAM_WRONG = 413;
        /** 服务器异常 */
        public static final int SERVER_EXCEPTION = 500;
    }

    public static final class RequestMsg{
        public static final String EMPTY_PARAM = "接受到的参数为空.";
        public static final String AUTH_INFO = "授权信息不完整";
        public static final String CUSTOMER_WRONG = "客户不存在";
        public static final String CUSTOMER_NOT_OPEN = "客户没有被启用";
        public static final String API_NOT_AUTH = "没有进行API授权";
        public static final String API_NOT_OPEN = "API授权状态未启用";
        public static final String TOKEN_ERROR = "Token错误,授权失败";
        public static final String WRONG_PLATFORM = "没有此平台";
        public static final String PLATFORM_CANNOT_CALL = "当前平台无法调用该接口";
        public static final String SUCCESS = "操作成功";
        public static final String SERVER_EXCEPTION = "服务器异常";


        public static final String ASIN_LIST_EMPTY = "Asin列表为空";

    }
}
