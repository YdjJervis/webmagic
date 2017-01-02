package com.eccang.spider.downloader;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

import java.util.concurrent.TimeUnit;

/**
 * 火狐内核下载器
 */
public class FireFoxDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private WebDriver mWebDriver;
    // 2解析机器上浏览器路径
    String BIN_NAME = "firefox_binary";
    private String mDriverPath = "E:\\softsare\\web245\\hhllq_Firefox_gr\\App\\Firefox\\firefox.exe";

    private String mIPPort = "172.16.7.144:9090";// 解析机器,选择远程的浏览器
    private long mSleepTime = 0;
    private String mProxyIP;
    private int mProxtPort;

    public FireFoxDownloader(String driverPath) {
        System.setProperty("webdriver.firefox.bin", driverPath);
        mDriverPath = driverPath;
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = null;
        try {
            initDriver();
            mWebDriver.get(request.getUrl());

            sleep(mSleepTime);

            WebElement webElement = mWebDriver.findElement(By.xpath("/html"));
            String content = webElement.getAttribute("outerHTML");
            page = new Page();
            page.setRawText(content);
            page.setHtml(new Html(UrlUtils.fixAllRelativeHrefs(content, request.getUrl())));

            page.setUrl(new PlainText(request.getUrl()));
            page.setRequest(request);

        } catch (Exception e) {
            if (mWebDriver != null)
                mWebDriver.quit();
            mWebDriver = null;
            System.out.println("exception:" + e.getCause());
        }
        return page;
    }

    @Override
    public void setThread(int threadNum) {

    }

    /**
     * 初始化浏览器信息
     */
    private void initDriver() {

        if (mWebDriver == null) {
            try {

                //个性化设置,设置ua，可不设置
                FirefoxProfile profile = getProfile();

                DesiredCapabilities ffDesiredcap = DesiredCapabilities.firefox();
                ffDesiredcap.setCapability(BIN_NAME, mDriverPath);

                ffDesiredcap.setCapability("firefox_profile", profile);
//                mWebDriver = new RemoteWebDriver(new URL("http://" + mIPPort + "/wd/hub"), ffDesiredcap);
                mWebDriver = new FirefoxDriver(profile);

                mWebDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);

            } catch (Exception e) {
                if (mWebDriver != null)
                    mWebDriver.quit();
                mWebDriver = null;
                logger.error(e.toString());
            }
        }
    }

    private FirefoxProfile getProfile() {
        //个性化设置,设置ua，可不设置
        FirefoxProfile profile = new FirefoxProfile();

        //设置代理参数
        if (StringUtils.isNotEmpty(mProxyIP)) {
            logger.info("使用了代理下载页面：IP=" + mProxyIP + ",Port=" + mProxtPort);
            profile.setPreference("network.proxy.type", 1);
            profile.setPreference("network.proxy.http", mProxyIP);
            profile.setPreference("network.proxy.http_port", mProxtPort);
        }
        return profile;
    }

    /**
     * 设置代理，eg:172.16.7.144:9090
     */
    public FireFoxDownloader setIPPort(String ipPort) {
        mIPPort = ipPort;
        return this;
    }

    /**
     * 设置渲染时间
     */
    public FireFoxDownloader setSleepTime(int sleepTime) {
        mSleepTime = sleepTime;
        return this;
    }

    /**
     * 设置渲染时间
     */
    public FireFoxDownloader setProxy(String proxyIP, int proxyPort) {
        mProxyIP = proxyIP;
        mProxtPort = proxyPort;
        return this;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
    }
}
