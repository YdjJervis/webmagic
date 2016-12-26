package us.codecraft.webmagic.samples.amazon.service.dict;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.dict.APIDao;
import us.codecraft.webmagic.samples.amazon.pojo.dict.API;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class APIService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private APIDao mAPIDao;

    @Autowired
    private CustomerService mCustomerService;

    public API findByCode(String customerCode) {
        API api = mAPIDao.findByCode(customerCode);
        if (api != null) {
            api.customer = mCustomerService.findByCode(customerCode);
        }
        return api;
    }


}
