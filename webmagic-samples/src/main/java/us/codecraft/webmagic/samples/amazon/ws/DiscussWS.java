package us.codecraft.webmagic.samples.amazon.ws;

import us.codecraft.webmagic.samples.amazon.pojo.Discuss;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * DiscussWebService
 */
@WebService
public interface DiscussWS {

    @WebMethod
    List<Discuss> getAllDiscuss(String asin);

    @WebMethod
    List<Discuss> getAll();
}
