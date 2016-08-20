package us.codecraft.webmagic.netsense.stats.gov.pojo;

import us.codecraft.webmagic.netsense.base.pojo.BaseBean;

/**
 * 指标
 */
public class Target extends BaseBean{


    /**
     * cname : 商品住宅销售额_累计值
     * code : A140B01
     * dotcount : 2
     * exp : 商品房销售额指报告期内出售商品房屋的合同总价款(即双方签署的正式买卖合同中所确定的合同总价)。该指标与商品房销售面积同口径，由现房销售额和期房销售额两部分组成。住宅按照用途可以划分为经济适用住房和别墅、高档公寓等。按照户型结构可以划分为90平方米以下住房，144平方米以上住房等。
     * ifshowcode : false
     * memo : 2005年8月份以前的商品房销售面积和销售额为实际销售统计口径；2005年8月份以后的销售面积和销售额包括期房和现房。
     * name : 商品住宅销售额_累计值
     * nodesort : 1
     * sortcode : 9503
     * tag :
     * unit : 亿元
     */

    private String wdcode;
    private String wdname;
    private String code;
    private String cname;
    private String dotcount;
    private String exp;
    private boolean ifshowcode;
    private String memo;
    private String name;
    private String nodesort;
    private String sortcode;
    private String tag;
    private String unit;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDotcount() {
        return dotcount;
    }

    public void setDotcount(String dotcount) {
        this.dotcount = dotcount;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public boolean isIfshowcode() {
        return ifshowcode;
    }

    public void setIfshowcode(boolean ifshowcode) {
        this.ifshowcode = ifshowcode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodesort() {
        return nodesort;
    }

    public void setNodesort(String nodesort) {
        this.nodesort = nodesort;
    }

    public String getSortcode() {
        return sortcode;
    }

    public void setSortcode(String sortcode) {
        this.sortcode = sortcode;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWdcode() {
        return wdcode;
    }

    public String getWdname() {
        return wdname;
    }

    public void setWdcode(String wdcode) {
        this.wdcode = wdcode;
    }

    public void setWdname(String wdname) {
        this.wdname = wdname;
    }

    @Override
    public String toString() {
        return "Target{" +
                "wdcode='" + wdcode + '\'' +
                ", wdname='" + wdname + '\'' +
                ", code='" + code + '\'' +
                ", cname='" + cname + '\'' +
                ", dotcount='" + dotcount + '\'' +
                ", exp='" + exp + '\'' +
                ", ifshowcode=" + ifshowcode +
                ", memo='" + memo + '\'' +
                ", name='" + name + '\'' +
                ", nodesort='" + nodesort + '\'' +
                ", sortcode='" + sortcode + '\'' +
                ", tag='" + tag + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
