package us.codecraft.webmagic.samples.amazon.pojo.crawl;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.2
 * @Description: 关键词搜索
 * @date 2016/12/27 11:11
 */
public class KeywordRank {
    private int id;
    private String asin;
    private String keyword;
    private String siteCode;
    private String departmentCode;
    private int totalPages;
    private int everyPage;
    private int rankNum;
    private Date createTime;
    private Date updateTime;

    public KeywordRank(){}

    public KeywordRank(String asin, String keyword, String siteCode, String departmentCode) {
        this.asin = asin;
        this.keyword = keyword;
        this.siteCode = siteCode;
        this.departmentCode = departmentCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public int getRankNum() {
        return rankNum;
    }

    public void setRankNum(int rankNum) {
        this.rankNum = rankNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}