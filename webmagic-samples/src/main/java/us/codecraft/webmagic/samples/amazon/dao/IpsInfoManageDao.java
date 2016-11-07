package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.IpsInfoManage;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/10/27 11:18
 */
@Repository
public interface IpsInfoManageDao extends BaseDao<IpsInfoManage> {

    /**
     * 查询有效的IP信息
     *
     * @return
     */
    List<IpsInfoManage> findIsValidIps(String urlHost);

    /**
     * 获取当前正在使用的ip信息
     * @return
     */
    IpsInfoManage findIpInfoIsUsing(String urlHost);

    /**
     * 查询对应域名的IP管理信息
     * @param urlHost
     * @return
     */
    List<IpsInfoManage> findIpsInfoByUrlHost(String urlHost);

}
