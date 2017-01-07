package com.eccang.spider.amazon.dao;


import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.ImportData;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 模拟获取导入数据
 * 2016/12/5 20:00
 */
@Repository
public interface ImportDataDao {

    List<ImportData> findBySite(String siteCode, int limit);

    List<ImportData> findAll(int limit);
}