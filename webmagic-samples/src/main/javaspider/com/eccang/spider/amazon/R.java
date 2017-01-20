package com.eccang.spider.amazon;

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

        /**
         * top100品类爬取
         */
        public static final int TOP_100_DEPARTMENT = 6;

        /**
         * top100商品分类爬取
         */
        public static final int TOP_100_CLASSIFY = 7;

        /**
         * top100品类商品爬取
         */
        public static final int TOP_100_PRODUCT = 8;
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
        public static final String DE = "DE";
        public static final String FR = "FR";
        public static final String IT = "IT"; //意大利
        public static final String ES = "ES"; //西班牙
        public static final String JP = "JP";
        public static final String CA = "CA"; //加拿大
    }

    public static final class KeywordRank {
        public static final String MAX_PAGE_NUM = "maxPageNum"; /*最大页码数*/
        public static final String EVERY_PAGE_NUM = "everyPageNum"; /*每页商品数*/
        public static final String KEYWORDS = "keywords"; /*搜索关键词参数*/
        public static final String DEPARTMENT = "url"; /*搜索关键词品类参数*/
    }

}
