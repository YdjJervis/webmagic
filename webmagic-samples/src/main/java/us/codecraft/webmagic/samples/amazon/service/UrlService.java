package us.codecraft.webmagic.samples.amazon.service;

import java.util.List;

/**
 * URL Service
 */
public interface UrlService<T> {

    List<T> findAll();

    void update(T url);

    long addAll(List<T> urlList);
}
