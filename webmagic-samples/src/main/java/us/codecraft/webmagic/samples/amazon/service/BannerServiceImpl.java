package us.codecraft.webmagic.samples.amazon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.amazon.dao.BannerDao;
import us.codecraft.webmagic.samples.amazon.pojo.Banner;

import java.util.List;

@Service("bannerService")
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerDao dao;

    @Override
    public List<Banner> findAll() {
        return dao.findAll();
    }

    @Override
    public long add(Banner banner) {
        return dao.add(banner);
    }

    @Override
    public long addAll(List<Banner> bannerList) {
        return dao.addAll(bannerList);
    }

    @Override
    public List<Banner> findAllBySite(String site) {
        return dao.findAllBySite(site);
    }


}
