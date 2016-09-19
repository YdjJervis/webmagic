package us.codecraft.webmagic.samples.amazon.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;
import us.codecraft.webmagic.samples.base.dao.BaseDaoImpl;

@Repository(value = "discussDao")
@Transactional
public class DiscussDaoImpl extends BaseDaoImpl<Discuss> implements IDiscussDao{
}
