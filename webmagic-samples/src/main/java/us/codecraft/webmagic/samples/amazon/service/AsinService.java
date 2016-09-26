package us.codecraft.webmagic.samples.amazon.service;

import us.codecraft.webmagic.samples.amazon.pojo.Asin;

import java.util.List;

/**
 * Asin Service
 */
public interface AsinService {

    List<Asin> findAll();

    void update(Asin asin);
}
