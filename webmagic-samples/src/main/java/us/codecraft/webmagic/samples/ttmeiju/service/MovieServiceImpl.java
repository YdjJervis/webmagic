package us.codecraft.webmagic.samples.ttmeiju.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.samples.base.dao.BaseDao;
import us.codecraft.webmagic.samples.base.service.BaseServiceImpl;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Movie;

import javax.annotation.Resource;

/**
 * Movie Service的实现类 继承BaseServiceImpl
 */
@Service("movieService")
@Transactional
public class MovieServiceImpl extends BaseServiceImpl<Movie> implements IMovieService {

    /**
     * 注入DAO
     */
    @Resource(name = "movieDao")
    public void setDao(BaseDao<Movie> dao) {
        super.setDao(dao);
    }

    /**
     * 若CustomerService 定义了BaseService没有的方法，则可以在这里实现
     */

}
