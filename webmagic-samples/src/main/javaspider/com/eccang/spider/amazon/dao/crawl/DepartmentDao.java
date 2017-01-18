package com.eccang.spider.amazon.dao.crawl;

import com.eccang.spider.amazon.pojo.crawl.Department;
import com.eccang.spider.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Hardy
 * @version V0.2
 * @Description:
 * @date 2017/1/16 10:41
 */
@Repository
public interface DepartmentDao extends BaseDao<Department> {

    List<Department> findByName(String depName);

    Department findByNameAndUrl(String depName, String depUrl);

    Department findByNames(String depName, String pDepName);
}
