package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.RequestStat;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 请求统计 Dao层
 * @date 2016/10/11 18:00
 */
@Repository
public interface RequestStatDao extends BaseDao<RequestStat> {

    void addOnDuplicate(RequestStat stat);

    RequestStat findByConditionsCode(String conditionsCode);
}
