package com.eccang.spider.amazon.service.pay;

import com.eccang.spider.amazon.pojo.dict.PayProfile;
import com.eccang.spider.amazon.pojo.pay.PayPackageStub;
import com.eccang.spider.amazon.service.dict.PayProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jervis
 * @version V0.2
 * @Description: 套餐计算实现类
 * @date 2017/2/6 18:21
 */
@Service
public class PayCalculatorImpl implements PayCalculator {

    @Autowired
    private PayProfileService mPayProfileService;

    @Override
    public int calculate(List<PayPackageStub> payPackageStubList, int custom) {

        int price = 0;

        for (PayPackageStub payPackageStub : payPackageStubList) {
            price += calculate(payPackageStub, custom);
        }

        return price;
    }

    @Override
    public int calculate(PayPackageStub payPackageStub, int custom) {

        int totalUrl = 0;
        Double totalPrice;

        if (Business.ASIN_SPIDER.equals(payPackageStub.businessCode)) {

            int ra = 200;//ASIN平均Review数量
            int ua = ra / 10 + 1;//产生的URL数量
            int rua = 2;//每次更新爬取产生的URL数量
            totalUrl = ua + payPackageStub.day * 24 / payPackageStub.frequency * rua;//单个ASIN平均一周产生的总的URL数量

        } else if (Business.MONITOR_SPIDER.equals(payPackageStub.businessCode)) {

            int rua = 1;//每次产生的URL数量
            totalUrl = payPackageStub.day * 24 / payPackageStub.frequency * rua;//一条Review一周产生的URL总量公式如下

        } else if (Business.KEYWORD_RANK_SPIDER.equals(payPackageStub.businessCode)) {

            int ra = 10;//每个关键词平均翻页数量
            totalUrl = payPackageStub.day * 24 / payPackageStub.frequency * ra;//产生的URL总数

        } else if (Business.FOLLOW_SELL.equals(payPackageStub.businessCode)) {

            int fsa = 3;//每个ASIN平均跟卖页数
            totalUrl = payPackageStub.day * 24 / payPackageStub.frequency * fsa;//产生的URL总数

        }

        PayProfile payProfile = mPayProfileService.findByCode(Business.ASIN_SPIDER);
        float rcp = totalUrl * payProfile.urlPrice;//无优先级总的价格

        Double rcpp = rcp * Math.pow(payProfile.priorityMutiple, 4 - payPackageStub.priority);//带优先级时
        totalPrice = rcpp;

        if (custom == 1) {
            totalPrice = rcpp * payProfile.customMutiple;//属于自定义套餐的，应该再乘以一个基数
        }

        Double price = payPackageStub.count * totalPrice;

        return price.intValue() / 10 * 10;//让价格只等于10的倍数
    }

    public static final class Business {
        static final String ASIN_SPIDER = "AS";
        static final String MONITOR_SPIDER = "MS";
        static final String KEYWORD_RANK_SPIDER = "KRS";
        static final String FOLLOW_SELL = "FS";
    }
}
