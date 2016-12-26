package us.codecraft.webmagic.samples.amazon.service.dict;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.dict.CustomerDao;
import us.codecraft.webmagic.samples.amazon.pojo.dict.Customer;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 客户 业务
 * @date 2016/10/11
 */
@Service
public class CustomerService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private CustomerDao mCustomerDao;

    @Autowired
    private PlatformService mPlatformService;

    public Customer findByCode(String customerCode) {
        Customer customer = mCustomerDao.findByCode(customerCode);
        if (customer != null) {
            customer.platform = mPlatformService.findByCode(customer.platformCode);
        }
        return customer;
    }


}
