package us.codecraft.webmagic.samples.base.service;

import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.samples.base.dao.BaseDao;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;


/**
 * BaseServiceImpl 定义Service的通用操作的实现
 */
@Transactional
public class BaseServiceImpl<T> implements BaseService<T> {

    /**
     * 注入BaseDao
     */
    private BaseDao<T> dao;
    @Resource
    public void setDao(BaseDao<T> dao) {
        this.dao = dao;
    }

    public void save(T entity) {
        dao.save(entity);
    }

    public void save(List<T> list) {
        dao.save(list);
    }

    public void update(T entity) {
        dao.update(entity);
    }

    public void delete(Serializable id) {
        dao.delete(id);
    }

    public T getById(Serializable id) {
        return dao.findById(id);
    }

    public List<T> getByHQL(String hql, Object... params) {
        return dao.findByHQL(hql, params);
    }
}
