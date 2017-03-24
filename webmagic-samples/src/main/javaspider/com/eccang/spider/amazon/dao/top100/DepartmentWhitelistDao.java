package com.eccang.spider.amazon.dao.top100;

import com.eccang.spider.amazon.pojo.top100.DepartmentWhitelist;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/3/15 15:39
 */
@Repository
public interface DepartmentWhitelistDao extends BaseDao<DepartmentWhitelist> {

    DepartmentWhitelist findByDoubleCode(String depCode, String siteCode);

    int findByCodeCount(String depCode, String siteCode);

    void updateByCode(DepartmentWhitelist departmentWhitelist);

    void deleteByCode(String depCode, String siteCode);
}