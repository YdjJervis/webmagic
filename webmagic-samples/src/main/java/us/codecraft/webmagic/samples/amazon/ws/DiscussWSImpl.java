package us.codecraft.webmagic.samples.amazon.ws;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.samples.amazon.dao.DiscussDao;
import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.util.List;

/**
 * 评论WebService
 */
@WebService
public class DiscussWSImpl implements DiscussWS {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    DiscussDao mDiscussDao;

    @Override
    public List<Discuss> getAllDiscuss(String asin) {
        logger.info("getAllDiscuss(String asin)::asin=" + asin);
        return mDiscussDao.findAllByAsin(asin);
    }

    @Override
    public List<Discuss> getAll() {
        logger.info("getAll()::");
        return mDiscussDao.findAll();
    }

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/eccang/webservice/getAllDiscuss", new DiscussWSImpl());
    }
}
