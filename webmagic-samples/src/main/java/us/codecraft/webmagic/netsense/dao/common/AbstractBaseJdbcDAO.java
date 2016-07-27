package us.codecraft.webmagic.netsense.dao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract public class AbstractBaseJdbcDAO {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBaseJdbcDAO.class);

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AbstractBaseJdbcDAO(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * 统计批量更新成功的记录数
     *
     * @param updateStatus，记录更新状态，1:更新成功, 0:更新失败
     * @return 更新成功的记录数
     */
    protected int statUpdateSuccRecordNum(int[] updateStatus) {
        int num = 0;
        for (int status : updateStatus) {
            if (1 == status) {
                num++;
            }
        }
        return num;
    }

    public <T> int add(List<T> list, String sql) {

        logger.info(list.toString());
        final int batchNumber = 1500; // 一次批量更新记录数
        SqlParameterSource[] sqlParameterSources = new SqlParameterSource[list.size() > batchNumber ? batchNumber : list.size()];

        int updateNumber = 0;
        int i = 0;
        for (T entity : list) {

            sqlParameterSources[i++] = new BeanPropertySqlParameterSource(entity);
            if (i == batchNumber) { // 批量更新一次
                int[] updateStatus = this.getNamedParameterJdbcTemplate().batchUpdate(sql, sqlParameterSources);
                updateNumber += statUpdateSuccRecordNum(updateStatus);
                // 重置相关变量
                i = 0;
                sqlParameterSources = new SqlParameterSource[batchNumber];
            }
        }

        if (i > 0) { // 最后一次批量更新
            SqlParameterSource[] tmpSqlParameterSources = new SqlParameterSource[i];
            for (int j = 0; j < i; j++) {
                tmpSqlParameterSources[j] = sqlParameterSources[j];
            }
            int[] updateStatus = this.getNamedParameterJdbcTemplate().batchUpdate(sql, tmpSqlParameterSources);
            updateNumber += statUpdateSuccRecordNum(updateStatus);
        }

        logger.info("更新数量 = " + updateNumber);
        return updateNumber;
    }

    /**
     * 只有当表字段和类成员变量名一样的时候才能用，而且暂时不支持父类字段
     *
     * @param list       ORM模型对象数组
     * @param clazz      ORM类的class对象
     * @param tableName  表名
     * @param exceptions 不更新的字段名
     * @param <T>        要插入实体的泛型
     * @return 更新数量
     */
    public <T> int add(List<T> list, Class<T> clazz, String tableName, String... exceptions) {

//        String sql = "insert into t_ori_national_data_menu(wdcode, wdname,code,name,sort,issj,updatetime) values(:wdcode, :wdname, :code,:name,:sort,:issj,:updatetime )"
//                + " ON DUPLICATE KEY UPDATE wdcode=:wdcode, wdname=:wdname, code=:code,name=:name,sort=:sort,issj=:issj,updatetime=:updatetime";

        Set<String> set = new HashSet<String>();
        for (String exception : exceptions) {
            set.add(exception);
        }

        Field[] fields = clazz.getDeclaredFields();
        Field[] sFields = clazz.getSuperclass().getDeclaredFields();

        StringBuffer sb = new StringBuffer();
        sb.append("insert into ").append(tableName).append("(");

        //------------要插入的字段-------------------------------------------------
        for (Field sField : sFields) {
            sb.append(sField.getName()).append(",");
        }
        for (int i = 0; i < fields.length; i++) {

            if (!Modifier.isStatic(fields[i].getModifiers())) {
                sb.append(fields[i].getName());
                if (i != fields.length - 1) {
                    sb.append(",");
                }
            }
        }
        //------------要插入的字段-------------------------------------------------

        //------------要插入的字段的值---------------------------------------------
        sb.append(")values").append("(");
        for (Field sField : sFields) {
            sb.append(":").append(sField.getName()).append(",");
        }
        for (int i = 0; i < fields.length; i++) {//values

            if (!Modifier.isStatic(fields[i].getModifiers())) {
                sb.append(":").append(fields[i].getName());
                if (i != fields.length - 1) {
                    sb.append(",");
                }
            }
        }
        //------------要插入的字段的值---------------------------------------------

        //------------更新字段----------------------------------------------------
        sb.append(") ON DUPLICATE KEY UPDATE ");
        for (Field sField : sFields) {

            if (!set.contains(sField.getName())) {
                sb.append(sField.getName()).append("=:").append(sField.getName()).append(",");
            }
        }
        for (int i = 0; i < fields.length; i++) {//values
            if (!Modifier.isStatic(fields[i].getModifiers())) {

                if (!set.contains(fields[i].getName())) {
                    sb.append(fields[i].getName()).append("=:").append(fields[i].getName());
                    if (i != fields.length - 1) {
                        sb.append(",");
                    }
                }
            }
        }
        //------------更新字段----------------------------------------------------

        logger.info("sql=" + sb.toString());

        return add(list, sb.toString());
    }

    /**
     * @param table 表名
     * @param clazz 处理对象的Class对象
     * @param <T>   模板类
     * @return
     */
    protected <T> List<T> findAll(String table, Class clazz) {
        String sql = "select * from " + table;
        return getNamedParameterJdbcTemplate().query(sql, Collections.EMPTY_MAP, ParameterizedRowMapperFactory.getParameterizedRowMapper(clazz));
    }

    /**
     * 根据查询条件查找某些结果是否存在
     *
     * @param table  表名
     * @param clazz  处理对象的Class对象
     * @param fields 字段名数组
     * @param values 一一对应的字段值
     * @return 某个查询条件下的结果是否存在
     */
    protected boolean isExist(String table, Class clazz, String[] fields, String[] values) {
        List query = find(table, clazz, fields, values);
        return query.size() > 0;
    }

    /**
     * 根据查询条件返回查询结果
     *
     * @param table  表名
     * @param clazz  处理对象的Class对象
     * @param fields 字段名数组
     * @param values 一一对应的字段值
     * @param <T>    模板类
     * @return 查询列表
     */
    protected <T> List<T> find(String table, Class clazz, String[] fields, String[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(table).append(" where ");

        if (fields == null || values == null || (fields.length != values.length)) {
            throw new IllegalArgumentException("error : 参数不正确");
        }

        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                sb.append(" and ");
            }
            sb.append(fields[i]).append("='").append(values[i]).append("'");
        }
        return getNamedParameterJdbcTemplate().query(sb.toString(), Collections.EMPTY_MAP, ParameterizedRowMapperFactory.getParameterizedRowMapper(clazz));
    }


}
