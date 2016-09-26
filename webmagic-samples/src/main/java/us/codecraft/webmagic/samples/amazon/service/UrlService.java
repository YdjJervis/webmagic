package us.codecraft.webmagic.samples.amazon.service;

import us.codecraft.webmagic.samples.amazon.pojo.Url;

import java.util.List;

/**
 * URL Service
 */
public interface UrlService {

    List<Url> findAll();

    void update(Url url);

    long addAll(List<Url> urlList);
}
