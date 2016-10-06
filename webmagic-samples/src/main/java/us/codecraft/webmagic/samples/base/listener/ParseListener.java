package us.codecraft.webmagic.samples.base.listener;

import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.dao.UrlDao;
import us.codecraft.webmagic.samples.base.pojo.BaseUrl;

import java.util.List;

/**
 * 把源数据转换成需要爬去的目标URL
 */
public abstract class ParseListener implements ScheduledTask {

    @Autowired
    private UrlDao mUrlDao;

    @Override
    public void execute() {
//        mUrlDao.addAll(/*getUrl()*/);
    }

    /**
     * 把数据列表源转换成URL列表，数据来源不同，转换规则不同，所以需要
     * 子类单独处理
     */
    protected abstract <T extends BaseUrl> List<T> getUrl();
}
