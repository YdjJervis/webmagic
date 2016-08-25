package us.codecraft.webmagic.netsense.stats.gov.dao;

import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Navigation;

import javax.sql.DataSource;
import java.util.List;

/**
 * 指标
 */
public class NavigationDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_stats_gov_nav";

    public NavigationDao(DataSource dataSource) {
        super(dataSource);
    }

    public int add(List<Navigation> list) {
        return super.add(list, Navigation.class, TABLE_NAME);
    }
}
