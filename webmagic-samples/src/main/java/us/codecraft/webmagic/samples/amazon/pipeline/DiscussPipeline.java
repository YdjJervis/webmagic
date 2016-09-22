package us.codecraft.webmagic.samples.amazon.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.dao.DiscussDao;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.base.Context;

import java.util.List;

public class DiscussPipeline implements Pipeline {

    public static final String PARAM_LIST = "param_list";
    public static final String PARAM_DISCUSS = "param_discuss";

    private Logger logger = Logger.getLogger(getClass());


    private ApplicationContext mContext = Context.getInstance();

    @Override
    public void process(ResultItems resultItems, Task task) {
        DiscussDao discussDao = (DiscussDao) mContext.getBean("discussDao");

        List<Discuss> discussList = resultItems.get(PARAM_LIST);
        if (CollectionUtils.isNotEmpty(discussList)) {
            logger.info(discussList.toString());
            for (Discuss discuss : discussList) {
                try {
                    discussDao.add(discuss);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
