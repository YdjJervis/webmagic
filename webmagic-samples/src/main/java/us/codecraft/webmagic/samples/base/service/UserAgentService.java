package us.codecraft.webmagic.samples.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.base.dao.UserAgentDao;
import us.codecraft.webmagic.samples.base.pojo.UserAgent;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/14 10:59
 */
@Service
public class UserAgentService {

    @Autowired
    private UserAgentDao mDao;

    public List<UserAgent> findAll(){
        return mDao.findAll();
    }
}