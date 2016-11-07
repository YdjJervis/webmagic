package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.IpsSwitchManage;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/31 10:15
 */
@Repository
public interface IpsSwitchManageDao extends BaseDao<IpsSwitchManage> {

    /**
     * 查询是否完成的IP切换管理信息（按时间分批次管理）
     * @return IpsSwitchManage集合
     */
    List<IpsSwitchManage> findByIsComplete(int isComplete);

    /**
     * 通过id更新切换管理信息
     * @param ipsSwitchManage 切换IP管理信息
     */
    void updateById(IpsSwitchManage ipsSwitchManage);

}
