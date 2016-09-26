package us.codecraft.webmagic.samples.base.listener;

/**
 * 把源数据转换成需要爬去的目标URL
 */
public interface ParseListener extends Runnable {

    void onParse();
}
