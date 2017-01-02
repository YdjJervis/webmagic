package com.eccang.spider.base.pojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 代理服务器
 * @date 2016/10/14 9:57
 */
public class ProxyServer extends BasePojo {

    public String hostName;
    public int port;
    /**
     * 使用次数
     */
    public int useCount;
    /**
     * 状态。1-可用；0-不可用
     */
    public int status;

    @Override
    public String toString() {
        return super.toString() + "ProxyServer{" +
                "hostName='" + hostName + '\'' +
                ", port=" + port +
                ", useCount=" + useCount +
                ", status=" + status +
                '}';
    }
}