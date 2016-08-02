package us.codecraft.webmagic.samples.base.service;


import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

    /**
     * BaseService 定义Service的通用操作
     *
     * @author Monday
     */
    void save(T entity);

    void save(List<T> list);

    void update(T entity);

    void delete(Serializable id);

    T getById(Serializable id);

    List<T> getByHQL(String hql, Object... params);

}
