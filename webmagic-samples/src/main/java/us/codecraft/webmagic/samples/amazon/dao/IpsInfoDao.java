package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfo;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 10:22
 */
@Repository
public interface IpsInfoDao extends BaseDao {

    /**
     * 通过id查询ip信息
     */
    IpsInfo findById(int id);

    List<IpsInfo> findByHost(String host);

}
