package us.codecraft.webmagic.samples.amazon.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.dao.BannerDao;
import us.codecraft.webmagic.samples.amazon.pojo.Banner;
import us.codecraft.webmagic.samples.base.Context;

import java.util.List;

public class BannerPipeline implements Pipeline {

    public static final String PARAM_LIST = "param_list";

    private Logger logger = Logger.getLogger(getClass());


    private ApplicationContext mContext = Context.getInstance();

    @Override
    public void process(ResultItems resultItems, Task task) {
        BannerDao bannerDao = (BannerDao) mContext.getBean("bannerDao");

        List<Banner> bannerList = resultItems.get(PARAM_LIST);
        if (CollectionUtils.isNotEmpty(bannerList)) {
            logger.info(bannerList.toString());
            for (Banner banner : bannerList) {
                if(StringUtils.isNotEmpty(banner.getSort())) {
                    bannerDao.add(banner);
                }
            }
        }
    }
}
