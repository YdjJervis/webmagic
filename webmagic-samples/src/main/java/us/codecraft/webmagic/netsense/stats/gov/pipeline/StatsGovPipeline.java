package us.codecraft.webmagic.netsense.stats.gov.pipeline;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.base.util.UrlUtils;
import us.codecraft.webmagic.netsense.stats.gov.dao.NavigationDao;
import us.codecraft.webmagic.netsense.stats.gov.dao.StatsGovDao;
import us.codecraft.webmagic.netsense.stats.gov.dao.TargetDao;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Navigation;
import us.codecraft.webmagic.netsense.stats.gov.pojo.QueryData;
import us.codecraft.webmagic.netsense.stats.gov.pojo.StatsGov;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Target;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

public class StatsGovPipeline implements Pipeline {

    public static final String PARAM_RESULT = "PARAM_RESULT";
    public static final String PARAM_NAVIGATION = "PARAM_NAVIGATION";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(ResultItems resultItems, Task task) {

        QueryData queryData = resultItems.get(PARAM_RESULT);
        List<Navigation> navigationList = resultItems.get(PARAM_NAVIGATION);
        if (queryData != null) {
            logger.info(queryData.getUrl());
            List<StatsGov> statsGovList = new ArrayList<StatsGov>();
            List<Target> targetList = new ArrayList<Target>();

            String dbcode = "";
            for (QueryData.Datanodes datanodes : queryData.getDatanodes()) {
                StatsGov statsGov = new StatsGov();
                statsGov.setCode(datanodes.getCode());
                statsGov.setData(datanodes.getData().getData());
                statsGov.setDotcount(datanodes.getData().getDotcount());
                statsGov.setHasdata(datanodes.getData().isHasdata());
                statsGov.setUrl(queryData.getUrl());

                dbcode = UrlUtils.getValue(statsGov.getUrl(), "dbcode");
                statsGov.setDbcode(dbcode);
                statsGovList.add(statsGov);
            }

            for (QueryData.Wdnodes wdnodes : queryData.getWdnodes()) {
                for (QueryData.Wdnodes.Nodes nodes : wdnodes.getNodes()) {
                    Target target = new Target();
                    target.setCode(nodes.getCode());
                    target.setDbcode(dbcode);
                    target.setWdcode(wdnodes.getWdcode());
                    target.setWdname(wdnodes.getWdname());
                    target.setDotcount(nodes.getDotcount());
                    target.setCname(nodes.getCname());
                    target.setExp(nodes.getExp());
                    target.setIfshowcode(nodes.isIfshowcode());
                    target.setMemo(nodes.getMemo());
                    target.setName(nodes.getName());
                    target.setNodesort(nodes.getNodesort());
                    target.setSortcode(nodes.getSortcode());
                    target.setTag(nodes.getTag());
                    target.setUnit(nodes.getUnit());
                    targetList.add(target);
                }
            }

            TargetDao targetDao = (TargetDao) Context.getInstance().getBean("targetDao");
            StatsGovDao statsGovDao = (StatsGovDao) Context.getInstance().getBean("statsGovDao");
            targetDao.add(targetList);
            statsGovDao.add(statsGovList);
        }
        if (navigationList != null) {
            NavigationDao dao = (NavigationDao) Context.getInstance().getBean("navigationDao");
            dao.add(navigationList);
        }
    }
}
