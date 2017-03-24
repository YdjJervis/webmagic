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

    int findChildDepCount(String pId, String batchNum);

    List<Department> findByPId(int pId);

    Department findByBatchNumAndDepUrl(String batchNum, String depUrl);

    Department findByBatchNumAndDepCode(String batchNum, String depCode);
}
