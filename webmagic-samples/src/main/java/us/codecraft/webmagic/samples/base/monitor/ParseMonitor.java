package us.codecraft.webmagic.samples.base.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import java.util.List;

/**
 * 把源数据转换成需要爬去的目标URL
 */
public abstract class ParseMonitor implements ScheduledTask {

    @Autowired
    private UrlService mUrlService;

    @Override
    public void execute() {
        mUrlService.addAll(getUrl());
    }

    /**
     * 把数据列表源转换成URL列表，数据来源不同，转换规则不同，所以需要
     * 子类单独处理
     */
    protected abstract List<Url> getUrl();
}
