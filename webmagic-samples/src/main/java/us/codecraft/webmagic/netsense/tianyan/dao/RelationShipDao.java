package us.codecraft.webmagic.netsense.tianyan.dao;

import us.codecraft.webmagic.netsense.dao.common.AbstractBaseJdbcDAO;
import us.codecraft.webmagic.netsense.tianyan.pojo.RelationShip;

import javax.sql.DataSource;
import java.util.List;

/**
 * 公司投资关系
 */
public class RelationShipDao extends AbstractBaseJdbcDAO {

    public RelationShipDao(DataSource dataSource) {
        super(dataSource);
    }

    public void add(List<RelationShip> list) {
        super.add(list, RelationShip.class, "t_ori_company_relation_qichacha");
    }

}
