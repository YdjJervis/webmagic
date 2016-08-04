package us.codecraft.webmagic.netsense.tianyan.pipeline;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.netsense.Context;
import us.codecraft.webmagic.netsense.tianyan.dao.CompanyDao;
import us.codecraft.webmagic.netsense.tianyan.dao.RelationShipDao;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;
import us.codecraft.webmagic.netsense.tianyan.processor.DetailsProcessor;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * 天眼查详情
 */
public class DetailsPipeline implements Pipeline {

    private static final String TAG = "DetailsPipeline::";

    private static CompanyDao mCompanyDao;
    private static RelationShipDao mRelationShipDao;

    static {
        mCompanyDao = (CompanyDao) Context.getInstance().getBean("companyDao");
        mRelationShipDao = (RelationShipDao) Context.getInstance().getBean("relationShipDao");
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        //公司详情入库
        CompanyInfo companyInfo = resultItems.get(DetailsProcessor.DETAILS);
        if (null != companyInfo && StringUtils.isNotEmpty(companyInfo.getName())) {
            mCompanyDao.add(companyInfo);
        }

        //对外投资列表入库
        List<RelationShip> list = resultItems.get(DetailsProcessor.LIST);
        if (CollectionUtils.isNotEmpty(list)) {
            System.out.println(TAG + list);
            mRelationShipDao.add(list);
        }

    }
}
