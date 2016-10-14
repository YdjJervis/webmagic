package us.codecraft.webmagic.samples.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.samples.base.dao.ProxyServerDao;
import us.codecraft.webmagic.samples.base.pojo.ProxyServer;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 代理服务器业务
 * @date 2016/10/14 10:59
 */
@Service
public class ProxyServerService {

    @Autowired
    private ProxyServerDao mDao;

    public List<ProxyServer> findAll() {
        return mDao.findAll();
    }

    /**
     * 更新ProxyServer对象。使用次数增加1，更新时间刷新
     */
    public void update(ProxyServer proxyServer) {
        proxyServer.useCount++;
        proxyServer.updatetime = new Date();
        mDao.update(proxyServer);
    }

    /**
     * 查找随机一个ProxyServer对象，并且更新它的使用次数
     */
    public ProxyServer findRandomProxy() {
        List<ProxyServer> list = findAll();
        int index = new Random().nextInt(list.size());
        ProxyServer ua = list.get(index);
        update(ua);
        return ua;
    }
}