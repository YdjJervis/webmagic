package us.codecraft.webmagic.netsense.tianyan.pipeline;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 天眼查详情
 */
public class CompanyPipeline implements Pipeline {

    private static final String TAG = "CompanyPipeline::";

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("netsence/applicationContext.xml");
    }

    @Override
    public void process(ResultItems resultItems, Task task) {



    }
}
