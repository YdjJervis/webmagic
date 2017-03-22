package com.eccang.spider.amazon.dao.dict;

import com.eccang.spider.amazon.pojo.dict.Profile;
import com.eccang.spider.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.2
 * @Description:
 * @date 2017/3/22 14:39
 */
public interface ProfileDao extends BaseDao<Profile>{

    Profile find();

}
