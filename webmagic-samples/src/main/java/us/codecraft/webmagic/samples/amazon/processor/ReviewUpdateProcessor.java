package us.codecraft.webmagic.samples.amazon.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.samples.amazon.service.AsinService;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 评论更新爬取业务
 * @date 2016/10/14 15:24
 */
@Service
public class ReviewUpdateProcessor extends ReviewProcessor {

    @Autowired
    private AsinService mAsinService;

    @Override
    public void process(Page page) {
        dealValidate(page);
    }

    @Override
    public void execute() {

    }
}