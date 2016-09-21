package us.codecraft.webmagic.samples.amazon.service;

import us.codecraft.webmagic.samples.amazon.pojo.Banner;

import java.util.List;

/**
 * 评论Service
 */
public interface BannerService {

    List<Banner> findAll();

    long add(Banner banner);

    long addAll(List<Banner> discussList);

    List<Banner> findAllBySite(String site);
}
