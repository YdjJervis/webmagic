package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Banner;

import java.util.List;
/**
 * @author Jervis
 * @version V0.1
 * @Description: 首页幻灯片轮播Dao
 * @date 2016/10/11 18:00
 */
@Repository
public interface BannerDao {

    List<Banner> findAll();

    List<Banner> findAllBySite(String site);

    /**
     * @return ASIN code
     */
    long add(Banner banner);

    long addAll(List<Banner> bannerList);


}
