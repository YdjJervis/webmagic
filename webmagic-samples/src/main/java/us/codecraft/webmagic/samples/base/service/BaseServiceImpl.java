package us.codecraft.webmagic.samples.base.service;

/**
 * 基础业务模型实现类
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    @Override
    public long delete(Integer id) {
        return 0;
    }

    @Override
    public T find(Integer id) {
        return null;
    }
}
