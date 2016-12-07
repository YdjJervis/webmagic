package us.codecraft.webmagic.samples.amazon.dao;


import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.ImportData;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * 模拟获取导入数据
 * 2016/12/5 20:00
 */
@Repository
public interface ImportDataDao {

    List<ImportData> findBySite(String siteCode);

    List<ImportData> findAll();
}