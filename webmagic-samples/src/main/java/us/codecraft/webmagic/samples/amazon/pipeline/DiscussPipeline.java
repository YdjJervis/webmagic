package us.codecraft.webmagic.samples.amazon.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

public class DiscussPipeline implements Pipeline {

    public static final String PARAM_LIST = "param_list";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(ResultItems resultItems, Task task) {

        List<Discuss> discussList = resultItems.get(PARAM_LIST);
        if (CollectionUtils.isNotEmpty(discussList)) {
            logger.info(discussList.toString());
        }
    }
}
