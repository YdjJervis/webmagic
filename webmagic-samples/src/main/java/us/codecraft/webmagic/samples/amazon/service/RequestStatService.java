package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.RequestStatDao;
import us.codecraft.webmagic.samples.amazon.pojo.RequestStat;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 请求统计业务
 * @date 2016/10/19
 */
@Service
public class RequestStatService {

    @Autowired
    private RequestStatDao mDao;

    public void addOnDuplicate(RequestStat stat) {
        mDao.addOnDuplicate(stat);
    }

    public RequestStat find(String conditionsCode) {
        return mDao.findByConditionsCode(conditionsCode);
    }
}
