package us.codecraft.webmagic.netsense.tianyan.dao;


import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.tianyan.pojo.CompanyInfo;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司
 */
public class CompanyDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_company_info";

    public CompanyDao(DataSource dataSource) {
        super(dataSource);
    }

    public void add(List<CompanyInfo> list) {
        add(list, CompanyInfo.class, TABLE_NAME);
    }

    public void add(CompanyInfo companyInfo) {
        List<CompanyInfo> list = new ArrayList<CompanyInfo>();

        list.add(companyInfo);

        add(list);
    }

    public List<CompanyInfo> findAll() {
        return findAll(TABLE_NAME, CompanyInfo.class);
    }

    public boolean isExist(String companyUrl) {
        return isExist(TABLE_NAME,CompanyInfo.class,new String[]{"url"},new String[]{companyUrl});
    }
}
