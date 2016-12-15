package com.eccang.util;

import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2016/12/15 16:49
 */
public class RegexUtil {

    public static final Set<String> mSiteCodeSet = Sets.newHashSet("CA", "US", "MX", "DE", "JP", "CN", "ES", "FR", "IN", "IT", "UK");
    public static final Set<String> mStatusSet = Sets.newHashSet("open", "close");

    /**
     * 验证星级表达式
     */
    public static boolean isStarRegex(String star) {
        return Pattern.compile("[0,1]-[0,1]-[0,1]-[0,1]-[0,1]").matcher(star).matches() || StringUtils.isEmpty(star);
    }

    /**
     * 验证优先级范围
     */
    public static boolean isPriorityQualified(int priority) {
        return priority >= 0 || priority <= 4;
    }

    public static boolean isSiteCodeQualified(String siteCode) {
        return StringUtils.isNotEmpty(siteCode) && mSiteCodeSet.contains(siteCode);
    }

    public static boolean isCrawlStatusQualified(String status) {
        return StringUtils.isNotEmpty(status) && mStatusSet.contains(status);
    }

    public static boolean isFrequencyQualified(int frequency) {
        return frequency > 0;
    }

    public static boolean isReviewIdQualified(String reviewId) {
        return StringUtils.isNotEmpty(reviewId);
    }

    public static boolean isCustomerCodeQualified(String customerCode) {
        return StringUtils.isNotEmpty(customerCode);
    }

    public static boolean isMonitorStatusQualified(int status) {
        return status == 0 || status == 1;
    }

    public static void main(String[] args) {
        System.out.println(isStarRegex("0-0-1-1-0"));
        System.out.println(isSiteCodeQualified("CN"));
    }
}
