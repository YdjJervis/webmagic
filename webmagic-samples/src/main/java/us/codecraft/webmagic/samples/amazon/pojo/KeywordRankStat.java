package us.codecraft.webmagic.samples.amazon.pojo;

import java.util.Date;

/**
 * @author Hardy
 * @version V0.1
 *  搜索的关键词排名信息
 *  2016/11/30 15:14
 */
public class KeywordRankStat {
    private int id;
    private int rankSearchId; /*关联的rank_search表的id*/
    private String rankNum; /*排名数*/
    private int totalPage; /*搜索显示的总页数*/
    private int everyPage; /*每一页商品数*/
    private boolean isComplete; /*是否完成排名监测*/
    private Date createDate;
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRankSearchId() {
        return rankSearchId;
    }

    public void setRankSearchId(int rankSearchId) {
        this.rankSearchId = rankSearchId;
    }

    public String getRankNum() {
        return rankNum;
    }

    public void setRankNum(String rankNum) {
        this.rankNum = rankNum;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getEveryPage() {
        return everyPage;
    }

    public void setEveryPage(int everyPage) {
        this.everyPage = everyPage;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}