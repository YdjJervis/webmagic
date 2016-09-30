package us.codecraft.webmagic.samples.base.service;

import java.util.List;

/**
 * 基础业务模型
 */
public interface BaseService<T> {

    /**
     * 增加一条数据库记录
     * @param obj 元素
     * @return 影响行数
     */
    long add(T obj);

    /**
     * 增加多条数据库记录
     * @param objList 元素列表
     * @return 影响行数
     */
    long addAll(List<T> objList);

    /**
     * 根据元素ID删除一条数据库记录
     * @param id 元素ID
     * @return 影响行数
     */
    long delete(Integer id);

    /**
     * 更新一条数据库记录
     * @param obj 元素
     * @return 影响行数
     */
    long update(T obj);

    /**
     * 根据ID查询一条数据库记录
     * @param id 元素ID
     * @return 元素对象
     */
    T find(Integer id);

    /**
     * 根据关键字查询元素列表
     * @param keyWord 查询条件
     * @return 元素列表
     */
    List<T> find(String keyWord);

    /**
     * @return 一个数据表所有记录
     */
    List<T> findAll();
}
