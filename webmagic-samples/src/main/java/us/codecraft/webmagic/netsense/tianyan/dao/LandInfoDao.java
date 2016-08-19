package us.codecraft.webmagic.netsense.tianyan.dao;


import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.tianyan.pojo.LandInfo;

import javax.sql.DataSource;
import java.util.List;

/**
 * 土地信息
 */
public class LandInfoDao extends AbstractBaseJdbcDAO {

    private static final String TABLE_NAME = "t_ori_landinfo";

    public LandInfoDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<LandInfo> findAll() {
        return findAll(TABLE_NAME, LandInfo.class);
    }

}
