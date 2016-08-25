package us.codecraft.webmagic.netsense.stats.gov.dao;

import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.stats.gov.pojo.Target;

import javax.sql.DataSource;
import java.util.List;

/**
 * 指标
 */
public class TargetDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_stats_gov_target";

    public TargetDao(DataSource dataSource) {
        super(dataSource);
    }

    public int add(List<Target> list) {
        return super.add(list, Target.class, TABLE_NAME);
    }
}
