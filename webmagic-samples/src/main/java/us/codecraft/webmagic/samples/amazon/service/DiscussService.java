package us.codecraft.webmagic.samples.amazon.service;

import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

/**
 * 评论Service
 */
public interface DiscussService {

    List<Discuss> findAll();

    long add(Discuss discuss);

    long addAll(List<Discuss> discussList);

    List<Discuss> findAllByAsin(String asin);
}
