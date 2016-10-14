package us.codecraft.webmagic.samples.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.base.dao.UserAgentDao;
import us.codecraft.webmagic.samples.base.pojo.UserAgent;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Jervis
 * @version V0.1
 * @Description: User-Agent业务
 * @date 2016/10/14 10:59
 */
@Service
public class UserAgentService {

    @Autowired
    private UserAgentDao mDao;

    public List<UserAgent> findAll() {
        return mDao.findAll();
    }

    /**
     * 更新UserAgent对象。使用次数增加1，更新时间刷新
     */
    public void update(UserAgent userAgent) {
        userAgent.useCount++;
        userAgent.updatetime = new Date();
        mDao.update(userAgent);
    }

    /**
     * 查找随机一个UserAgent对象，并且更新它的使用次数
     */
    public UserAgent findRandomUA() {
        List<UserAgent> agentList = findAll();
        int index = new Random().nextInt(agentList.size());
        UserAgent ua = agentList.get(index);
        update(ua);
        return ua;
    }
}