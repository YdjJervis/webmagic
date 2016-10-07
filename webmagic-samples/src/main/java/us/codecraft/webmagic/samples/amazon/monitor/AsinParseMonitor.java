package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.AsinDao;
import us.codecraft.webmagic.samples.amazon.pojo.Asin;
import us.codecraft.webmagic.samples.amazon.pojo.Url;
import us.codecraft.webmagic.samples.base.listener.ParseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个地方，把asin对象转换成URL对象后入库
 */
@Service
public class AsinParseMonitor extends ParseListener {

    @Autowired
    private AsinDao mAsinDao;

    @Override
    protected List<Url> getUrl() {
        List<Asin> asinList = mAsinDao.findAll();

        List<Url> urlList = new ArrayList<Url>();
        for (int i = 0, len = asinList.size(); i < len; i++) {
            Url url = new Url();
            Asin asin = asinList.get(i);
            url.url = "http://www.amazon.cn/dp/" + asin;
            urlList.add(url);
        }
        return urlList;
    }
}
