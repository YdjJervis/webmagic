package us.codecraft.webmagic.samples.base.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * BaseDaoImpl 定义DAO的通用操作的实现
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

    private Class<T> clazz;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 通过构造方法指定DAO的具体实现类
     */
    public BaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
        logger.info("DAO的真实实现类是：" + this.clazz.getName());
    }

    /**
     * 向DAO层注入SessionFactory
     */

    @Resource
    private SessionFactory sessionFactory;

    /**
     * 获取当前工作的Session
     */
    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void save(T entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void save(List<T> entity) {
        for (T t : entity) {
            save(t);
        }
    }

    public void update(T entity) {
        this.getSession().update(entity);
    }

    public void delete(Serializable id) {
        this.getSession().delete(this.findById(id));
    }

    public T findById(Serializable id) {
        return (T) this.getSession().get(this.clazz, id);
    }

    public List<T> findByHQL(String hql, Object... params) {
        Query query = this.getSession().createQuery(hql);
        for (int i = 0; params != null && i < params.length; i++) {
            query.setParameter(i, params[i]);
        }
        return query.list();
    }

    /*public List<T> findByHQL(String hql, String... params) {
        Query query = this.getSession().createQuery(hql);
        for (int i = 0; params != null && i < params.length; i++) {
            query.setString(i,param);
        }
        return query.list();
    }*/
}
