package us.codecraft.webmagic.samples.amazon;

/**
 * 常量定义类
 */
public final class R {

    /**
     * URL 爬取类型
     */
    public static final class CrawlType {
        /**
         * 爬取首页
         */
        public static final int REVIEW_MAIN_PAGE = 0;
        /**
         * Review全量爬取
         */
        public static final int REVIEW_ALL = 1;
        /**
         * Review更新爬取
         */
        public static final int REVIEW_UPDATE = 2;
        /**
         * Review监听爬取
         */
        public static final int REVIEW_MONITOR = 3;
        /**
         * 跟卖爬取
         */
        public static final int FOLLOW_SELL = 4;
        /**
         * 关键字搜索爬取
         */
        public static final int KEYWORD_RANK = 5;
    }

    /**
     * 批次爬取类型
     */
    public static final class BatchType {
        public static final int REVIEW_ALL = 0;
        public static final int REVIEW_MONITOR = 1;
        public static final int REVIEW_UPDATE = 2;
        public static final int FOLLOW_SELL = 3;
        public static final int KEYWORD_RANK = 4;
    }

    /**
     * 站点码
     */
    public static final class SiteCode {
        public static final String US = "US";
        public static final String UK = "UK";
    }

    public static final class KeywordRank {
        public static final String MAX_PAGE_NUM = "maxPageNum"; /*最大页码数*/
        public static final String EVERY_PAGE_NUM = "everyPageNum"; /*每页商品数*/
    }

}
