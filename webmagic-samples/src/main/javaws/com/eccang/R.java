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
        /** 数量限制 */
        public static final int COUNT_LIMIT = 414;
        /** 购买失败 */
        public static final int BUY_ERROR = 414;
    }

    public static final class RequestMsg{
        public static final String EMPTY_PARAM = "接受到的参数为空";
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

        public static final String LIST_PARAM_WRONG = "数据列表参数不正确";

        public static final String PARAMETER_POSITIVE_NUM_ERROR = "数据列表中,整型值应该为大于0";
        public static final String PARAMETER_FORMAT_ERROR = "参数格式不正确";

        public static final String PARAMETER_ASIN_SITECODE_ERROR = "数据列表中，没有传入正确的站点码";

        public static final String PARAMETER_ASIN_STAR_ERROR = "数据列表中,没有传入的爬取星级表达式不正确";

        public static final String PARAMETER_ASIN_PRIORITY_ERROR = "数据列表中,爬取优先级取值不正确,其范围应该为[0,4]";

        public static final String DATA_FORMAT_ERROR = "抱歉：data节点格式不正确";

        public static final String PARAMETER_DATA_NULL_ERROR = "数据列表内容为空";

        public static final String PARAMETER_ASIN_LIST_ERROR = "请求Asin中传存在空的Asin码";

        public static final String PARAMETER_ASIN_LIST_ASIN_ERROR = "数据列表中存在空数据";

//        public static final String PARAMETER_CRAWL_STATUS_ERROR = "爬取状态设置格式有误，爬取状态为\"open\"或\"close\"";

        public static final String PARAMETER_BATCH_NULL_ERROR = "查询的批次对象为空";

        public static final String PARAMETER_BATCH_NUM_ERROR = "查询的批次对象中的批次号为空";

        public static final String PARAMETER_REVIEW_ASIN_ERROR = "请求的Review中存在空的Asin码";

        public static final String PARAMETER_REVIEW_NULL_ERROR = "请求的Review中存在空的ReviewId";

        public static final String PARAMETER_STATUS_ERROR = "状态设置格式有误，正确范围为[0,1]";

        public static final String PARAMETER_ASIN_EMPTY__ERROR = "客户下，Asin不存在";

        public static final String PARAMETER_REVIEW_EMPTY__ERROR = "客户下，查询的监听Review不存在";

        public static final String PARAMETER_ASIN_NOT_WORK__ERROR = "此Asin还没有开始爬取";

        public static final String PARAMETER_REVIEW_NOT_EXIST__ERROR = "此评论不存在";

        public static final String PARAMETER_KEYWORD_ASIN_ERROR = "请求列表中存在空的Asin码";

        public static final String PARAMETER_KEYWORD_EMPTY__ERROR = "客户下，查询的监听keyword排名不存在";

        public static final String PARAMETER_KEYWORD_EMPTY = "关键词不能为空";

        public static final String PARAMETER_DEPARTMENT_CODE_EMPTY = "品类码不能为空";

        public static final String PARAMETER_DEPARTMENT_CODE_ERROR = "数据列表中，没有传入正确的品类码";

        public static final String QUERY_DATA_EMPTY = "查询到的数据为空";

        public static final String PARAMETER_QUERY_NULL_ERROR = "查询对象为空";

        public static final String PARAMETER_ASIN_EMPTY = "请求参数asin码不能为空";

        public static final String BATCH_NUM_WRONG = "批次号格式不正确";

        public static final String BUSSINESS_LIMIT = "数量超过该业务限制";
        public static final String PAY_PACKAGE_LIMIT = "数量超过套餐限制";
        public static final String PAY_PACKAGE_BUIED = "抱歉，已购买过此套餐";
        public static final String PAY_PACKAGE_MISSED = "抱歉，此套餐不存在";
        public static final String PAY_PACKAGE_NULL = "抱歉，套餐码不能为空";

        public static final String BUSINESS_MISSED = "抱歉，业务码不存在";
        public static final String DAY_LIMIT = "抱歉，天数超过限制";
        public static final String COUNT_LIMIT = "抱歉，数量超过限制";
        public static final String SAME_BUSINESS = "抱歉，导入有列表有相同业务";

    }

    /** 业务代码 */
    public static final class BusinessCode {
        public static final String ASIN_SPIDER = "AS"; /** 爬取asin */
        public static final String MONITOR_SPIDER = "MS"; /** 监听review */
        public static final String KEYWORD_RANK_SPIDER = "KRS"; /** 关键词排名搜索 */
        public static final String FOLLOW_SELL = "FS"; /** 关键词排名搜索 */
    }

    /** 业务统计 */
    public static final class BusinessInfo {
        public static final String USABLE_NUM = "usableNum"; /*可用数*/
        public static final String HAS_USED_NUM = "hasUsedNum"; /*已用数*/
    }
}

