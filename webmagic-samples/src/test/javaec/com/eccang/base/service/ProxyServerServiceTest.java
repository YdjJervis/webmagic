package com.eccang.base.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.eccang.base.SpringTestCase;
import com.eccang.spider.base.pojo.ProxyServer;
import com.eccang.spider.base.service.ProxyServerService;

import java.util.List;

/**
 * @author Jervis
 * @version V0.1
 * @Description: User-Agent业务测试
 * @date 2016/10/14 11:10
 */
public class ProxyServerServiceTest extends SpringTestCase {

    @Autowired
    private ProxyServerService mService;

    @Test
    public void testFindAll() {
        System.out.println(mService.findAll());
    }

    @Test
    public void testUpdate() {
        List<ProxyServer> list = mService.findAll();
        for (ProxyServer userAgent : list) {
            userAgent.useCount = -1;
            mService.update(userAgent);
        }
    }

    @Test
    public void testFindRandomProxy() {
        System.out.println(mService.findRandomProxy());
    }

    @Test
    public void testAdd() {
        ProxyServer server = new ProxyServer();
        server.hostName = "hostName";
        server.port = 8001;
        mService.add(server);
    }

}