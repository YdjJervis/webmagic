package us.codecraft.webmagic.netsense.tianyan.dao;


import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.tianyan.pojo.Company;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个表里存放天眼查所有公司的公司名信息，方便做爬取的时候有数据源
 */
public class CompanyNameDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_company_set";

    public CompanyNameDao(DataSource dataSource) {
        super(dataSource);
    }

    public void add(List<Company> list) {
        add(list, Company.class, TABLE_NAME);
    }

    public void add(Company company) {
        List<Company> list = new ArrayList<Company>();

        list.add(company);

        add(list);
    }

    public List<Company> findAll() {
        return findAll(TABLE_NAME, Company.class);
    }

}
