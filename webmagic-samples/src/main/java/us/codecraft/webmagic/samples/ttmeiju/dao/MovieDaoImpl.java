package us.codecraft.webmagic.samples.ttmeiju.dao;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.samples.base.dao.BaseDaoImpl;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Movie;

import java.util.List;

@Repository(value = "movieDao")
public class MovieDaoImpl extends BaseDaoImpl<Movie> implements IMovieDao {

    @Override
    public void save(Movie entity) {
        List<Movie> movieList = findByHQL("from Movie where name = ?", entity.getName());
        if (CollectionUtils.isNotEmpty(movieList)) {
            logger.info("数据库已存在记录(s)：size=" + movieList.size());
            Movie movie = movieList.get(0);
            movie.setImg(entity.getImg());
            movie.setType(entity.getType());
            movie.setSize(entity.getSize());
            movie.setDownload(entity.getDownload());
            movie.setDiscuss(entity.getDiscuss());
            movie.setWords(entity.getWords());
            movie.setUrl(entity.getUrl());
            movie.setUpdatetime(entity.getUpdatetime());
            movie.setSeri(entity.getSeri());
            movie.setIntroduce(entity.getIntroduce());
            super.update(movie);
        } else {
            super.save(entity);
        }

    }

}
