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
     * @param ipsStat
     * @return
     */
    public Integer addIpsStat(IpsStat ipsStat);

    /***
     * update ipsStat by id
     * @param ipsStat
     */
    public void updateIpsStatById(IpsStat ipsStat);

    /**
     * fina all
     * @return
     */
    public List<IpsStat> findIpsStatAll();

    /**
     * find  by id
     * @param id
     * @return
     */
    public IpsStat findIpsStatById(Integer id);

}
