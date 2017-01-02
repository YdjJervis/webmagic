package com.eccang.spider.amazon.dao.dict;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.dict.IpsInfoManage;
import com.eccang.spider.base.dao.BaseDao;

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
     */
    List<IpsInfoManage> findIsValidIps(String urlHost);

    /**
     * 获取当前正在使用的ip信息
     */
    List<IpsInfoManage> findIpInfoIsUsing(String urlHost);

    /**
     * 查询对应域名的IP管理信息
     */
    List<IpsInfoManage> findIpsInfoByUrlHost(String urlHost);

    /**
     *通过url域名和正在使用的状态来更新
     */
    void updateByUrlHost(IpsInfoManage ipsInfoManage);

}
