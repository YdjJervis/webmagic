package com.eccang.spider.amazon.dao.dict;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.dict.ProxyAgencyBaseInfo;

import java.util.List;

/**
 * @author Hardy
 * @version V0.1
 * @Description:
 * @date 2016/11/15 15:43
 */
@Repository
public interface ProxyAgencyBaseInfoDao {

    /**
     * 添加
     */
    void addInfo(ProxyAgencyBaseInfo proxyAgencyBaseInfo);

    /**
     * 通过id删除
     */
    void deleteById(int id);

    /**
     * 通过id更新
     */
    void updateById(ProxyAgencyBaseInfo proxyAgencyBaseInfo);

    /**
     * 通过id查询
     */
    ProxyAgencyBaseInfo findById(int id);

    /**
     * 通过代理类型查询
     */
    List<ProxyAgencyBaseInfo> findByCode(String proxyCode);

    /**
     * 查询所有代理商信息
     */
    List<ProxyAgencyBaseInfo> findAll();

}
