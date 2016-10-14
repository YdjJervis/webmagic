package us.codecraft.webmagic.samples.base.pojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 代理服务器
 * @date 2016/10/14 9:57
 */
public class ProxyServer extends BasePojo {

    public String hostName;
    public int port;
    public String useCount;

    @Override
    public String toString() {
        return super.toString() + "ProxyServer{" +
                ", hostName='" + hostName + '\'' +
                ", port=" + port +
                ", useCount='" + useCount + '\'' +
                '}';
    }
}