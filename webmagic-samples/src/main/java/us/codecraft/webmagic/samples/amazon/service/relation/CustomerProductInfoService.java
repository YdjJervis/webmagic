package us.codecraft.webmagic.samples.amazon.service.relation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.relation.CustomerProductInfoDao;
import us.codecraft.webmagic.samples.amazon.pojo.relation.CustomerProductInfo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户-产品基本信息关系业务
 * @date 2016/10/11
 */
@Service
public class CustomerProductInfoService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private CustomerProductInfoDao mDao;

    public void add(CustomerProductInfo customerProductInfo) {
        mDao.add(customerProductInfo);
    }
}
