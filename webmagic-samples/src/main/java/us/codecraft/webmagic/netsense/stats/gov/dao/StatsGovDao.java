package us.codecraft.webmagic.netsense.stats.gov.dao;

import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.stats.gov.pojo.StatsGov;

import javax.sql.DataSource;
import java.util.List;

/**
 * 表格数据
 */
public class StatsGovDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_stats_gov";

    public StatsGovDao(DataSource dataSource) {
        super(dataSource);
    }

    public int add(List<StatsGov> list) {
        return super.add(list, StatsGov.class, TABLE_NAME);
    }


}
