package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.amazon.pojo.Banner;

import java.util.List;

/**
 * 评论DAO
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
