package com.eccang.spider.amazon.dao.dict;

import org.springframework.stereotype.Repository;
import com.eccang.spider.amazon.pojo.dict.IpsInfo;
import com.eccang.spider.base.dao.BaseDao;

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
