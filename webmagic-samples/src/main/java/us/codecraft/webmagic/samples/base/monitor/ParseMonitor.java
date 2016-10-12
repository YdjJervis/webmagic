package us.codecraft.webmagic.samples.base.monitor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.amazon.service.AsinService;
import us.codecraft.webmagic.samples.amazon.service.UrlService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 把Asin转换成需要爬取的URL对象
 * @date 2016/10/11
 */
@Service
public abstract class ParseMonitor implements ScheduledTask {

    @Autowired
    private UrlService mUrlService;

    @Autowired
    private AsinService mAsinService;

    protected Logger sLogger = Logger.getLogger(getClass());

    @Override
    public void execute() {
        List<Url> urlList = getUrl();
        mUrlService.addAll(urlList);

        sLogger.info("更新ASIN状态...更新数量：" + urlList.size());
        if (CollectionUtils.isNotEmpty(urlList)) {
            for (Url url : urlList) {
                mAsinService.updateStatus(url.asin, false);
            }
        }
    }

    /**
     * 把数据列表源转换成URL列表，数据来源不同，转换规则不同，所以需要
     * 子类单独处理
     */
    protected abstract List<Url> getUrl();
}
