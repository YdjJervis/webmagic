package us.codecraft.webmagic.netsense.tianyan.dao;


import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.tianyan.pojo.Building;

import javax.sql.DataSource;
import java.util.List;

/**
 * 新房信息
 */
public class BuildingDao extends AbstractBaseJdbcDAO {

        private static final String TABLE_NAME = "t_ori_building";

    public BuildingDao(DataSource dataSource) {
        super(dataSource);
    }

    public List<Building> findAll() {
        return findAll(TABLE_NAME, Building.class);
    }

}
