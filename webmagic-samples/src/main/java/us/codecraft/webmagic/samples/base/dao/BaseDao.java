package us.codecraft.webmagic.samples.base.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Hibernate通用Dao
 */
public interface BaseDao<T> {

    void save(T entity);

    void save(List<T> entity);

    void update(T entity);

    void delete(Serializable id);

    T findById(Serializable id);

    List<T> findByHQL(String hql, Object... params);


}
