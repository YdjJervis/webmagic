package us.codecraft.webmagic.samples.amazon.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.PlatformDao;
import us.codecraft.webmagic.samples.amazon.pojo.Platform;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 平台 业务
 * @date 2016/10/11
 */
@Service
public class PlatformService {

    private Logger mLogger = Logger.getLogger(getClass());

    @Autowired
    private PlatformDao mPlatformDao;

    public Platform findByCode(String platformCode) {
        return mPlatformDao.findByCode(platformCode);
    }


}
