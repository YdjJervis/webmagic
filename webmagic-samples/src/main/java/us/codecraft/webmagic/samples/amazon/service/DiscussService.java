package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import java.util.List;

/**
 * 评论Service
 */
@Service
public interface DiscussService {

    List<Discuss> findAll();

    long add(Discuss discuss);

    long addAll(List<Discuss> discussList);

    List<Discuss> findAllByAsin(String asin);
}
