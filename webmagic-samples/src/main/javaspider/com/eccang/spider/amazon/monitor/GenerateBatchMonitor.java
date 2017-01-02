package com.eccang.spider.amazon.monitor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.spider.amazon.pojo.relation.CustomerAsin;
import com.eccang.spider.amazon.pojo.relation.CustomerFollowSell;
import com.eccang.spider.amazon.pojo.relation.CustomerKeywordRank;
import com.eccang.spider.amazon.pojo.relation.CustomerReview;
import com.eccang.spider.amazon.service.NoSellService;
import com.eccang.spider.amazon.service.batch.BatchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hardy
 * @version V0.2
 * @Description 自动检测生成批次子类
 * @date 2016/12/7 10:10
 */
public class GenerateBatchMonitor {

    protected Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    protected BatchService mBatchService;

    @Autowired
    protected NoSellService mNoSellService;

    <T> Map<String, List<T>> initCustomerListMap(List<T> srcList) {
        Map<String, List<T>> customerListMap = new HashMap<>();

        for (T item : srcList) {
            String customerCode = "";
            if (item instanceof CustomerReview) {
                customerCode = ((CustomerReview) item).customerCode;
            } else if (item instanceof CustomerAsin) {
                customerCode = ((CustomerAsin) item).customerCode;
            } else if (item instanceof CustomerFollowSell) {
                customerCode = ((CustomerFollowSell) item).customerCode;
            } else if (item instanceof CustomerKeywordRank) {
                customerCode = ((CustomerKeywordRank)item).getCustomerCode();
            }

            if (customerListMap.get(customerCode) == null) {
                customerListMap.put(customerCode, new ArrayList<T>());
            }

            customerListMap.get(customerCode).add(item);
        }
        return customerListMap;
    }
}