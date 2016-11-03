package us.codecraft.webmagic.samples.amazon.monitor;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 更新爬取Asin转换成Url
 * @date 2016/10/15
 */
@Service
public class UpdateParseMonitor extends AsinParseMonitor {

    @Override
    public void execute() {
        List<Url> urlList = getUrl(false);
        mUrlService.addAll(urlList);

        for (Url url : urlList) {
            url.asin.saaIsUpdatting = 1;
            mAsinService.udpate(url.asin);
        }
    }

}
