package com.eccang.spider.amazon.dao.top100;

import com.eccang.spider.amazon.pojo.top100.DepartmentBlacklist;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 10:55
 */
@Repository
public interface DepartmentBlacklistDao extends BaseDao<DepartmentBlacklist> {

    DepartmentBlacklist findByDoubleCode(String depCode, String siteCode);

    int findByDepCodeCount(String depCode, String siteCode);

    void deleteByCode(String depCode, String siteCode);

}
