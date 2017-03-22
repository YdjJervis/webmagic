package com.eccang.spider.base.monitor;

import com.eccang.spider.amazon.pojo.Url;
import com.eccang.spider.amazon.service.dict.CustomerService;
import com.eccang.spider.amazon.service.dict.ProfileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 把Asin转换成需要爬取的URL对象
 * @date 2016/10/11
 */
@Service
public abstract class ParseMonitor implements ScheduledTask {

    protected Logger sLogger = Logger.getLogger(getClass());

    @Autowired
    protected ProfileService mProfileService;
    @Autowired
    protected CustomerService mCustomerService;
    /**
     * 把数据列表源转换成URL列表，数据来源不同，转换规则不同，所以需要
     * 子类单独处理
     */
    protected abstract List<Url> getUrl(boolean isCrawlAll);

}
