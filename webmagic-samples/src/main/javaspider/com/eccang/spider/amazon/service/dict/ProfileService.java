package com.eccang.spider.amazon.service.dict;

import com.eccang.spider.amazon.dao.dict.ProfileDao;
import com.eccang.spider.amazon.pojo.dict.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/3/22 14:45
 */
@Service
public class ProfileService {

    @Autowired
    private ProfileDao mDao;

    public Profile find(){
        return mDao.find();
    }
}
