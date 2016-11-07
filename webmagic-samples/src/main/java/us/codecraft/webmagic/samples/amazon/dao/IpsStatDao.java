package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.IpsStat;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description:
 * @date 2016/10/24 11:33
 */
@Repository
public interface IpsStatDao {

    /***
     * add
     * @param ipsStat IpsStat对象
     * @return 返回影响行
     */
    Integer addIpsStat(IpsStat ipsStat);

    /***
     * update ipsStat by id
     * @param ipsStat IpsStat对象
     */
    void updateIpsStatById(IpsStat ipsStat);

    /**
     * fina all
     * @return IpsStat对象集合
     */
    List<IpsStat> findIpsStatAll();

    /**
     * find  by id
     * @param id id
     * @return IpsStat对象
     */
    IpsStat findIpsStatById(Integer id);

    /**
     * find by condition
     * @param condition 代理类型
     * @return IpsStat对象
     */
    IpsStat findByCondition(String condition);
}
