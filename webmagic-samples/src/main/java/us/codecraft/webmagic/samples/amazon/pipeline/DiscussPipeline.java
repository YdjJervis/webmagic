package us.codecraft.webmagic.samples.amazon.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.dao.DiscussDao;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.base.Context;

public class DiscussPipeline implements Pipeline {

    public static final String PARAM_LIST = "param_list";
    public static final String PARAM_DISCUSS = "param_discuss";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext mContext = Context.getInstance();

    @Override
    public void process(ResultItems resultItems, Task task) {

        DiscussDao discussDao = (DiscussDao) mContext.getBean("discussDao");

        /*List<Discuss> discussList = resultItems.get(PARAM_LIST);
        if (CollectionUtils.isNotEmpty(discussList)) {
            logger.info(discussList.toString());
            discussDao.addAll(discussList);
        }*/

        //单个评论入库，而不是单页，避免一个评论乱码，整页评论都丢失
        Discuss discuss = resultItems.get(PARAM_DISCUSS);
        if (discuss != null) {
            discussDao.add(discuss);
        }
    }
}
