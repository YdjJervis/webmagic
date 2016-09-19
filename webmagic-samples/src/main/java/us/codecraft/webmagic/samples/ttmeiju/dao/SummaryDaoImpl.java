package us.codecraft.webmagic.samples.ttmeiju.dao;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.samples.base.dao.BaseDaoImpl;
import us.codecraft.webmagic.samples.ttmeiju.pojo.Summary;

import java.util.List;

@Repository(value = "summaryDao")
@Transactional
public class SummaryDaoImpl extends BaseDaoImpl<Summary> implements ISummaryDao{

    @Override
    public void save(Summary entity) {
        List<Summary> list = findByHQL("from Summary where name = ?", entity.getName());
        if(CollectionUtils.isNotEmpty(list)){
            logger.info("数据库已存在记录(s)：size="+list.size());
            Summary summary = list.get(0);
            summary.setLeftTime(entity.getLeftTime());
            summary.setBackTime(entity.getBackTime());
            summary.setUrl(entity.getUrl());
            summary.setUpdatetime(entity.getUpdatetime());
            summary.setSeri(entity.getSeri());
            summary.setSeri(entity.getSeri());
            summary.setUpdateDate(entity.getUpdateDate());
            super.update(summary);
        }else{
            super.save(entity);
        }

    }
}
