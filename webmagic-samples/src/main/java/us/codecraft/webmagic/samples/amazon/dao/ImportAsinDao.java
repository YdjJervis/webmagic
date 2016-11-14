package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.ImportAsin;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Jervis
 * @version V0.20
 * @Description: 批次产生的ASIN进度 DAO
 * @date 2016/11/10 14:45
 */
@Repository
public interface ImportAsinDao extends BaseDao<ImportAsin> {

    List<ImportAsin> findAll(int limit);
}