package us.codecraft.webmagic.netsense.stats.gov.pojo;

import java.util.List;

public class OtherWds {


    /**
     * issj : false
     * nodes : [{"code":"A01010101","name":"居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010102","name":"食品烟酒类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010103","name":"衣着类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010104","name":"居住类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010105","name":"生活用品及服务类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010106","name":"交通和通信类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010107","name":"教育文化和娱乐类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010108","name":"医疗保健类居民消费价格指数(上年同月=100)","sort":"1"},{"code":"A01010109","name":"其他用品和服务类居民消费价格指数(上年同月=100)","sort":"1"}]
     * selcode :
     * wdcode : zb
     * wdname : 指标
     */

    private boolean issj;
    private String selcode;
    private String wdcode;
    private String wdname;
    /**
     * code : A01010101
     * name : 居民消费价格指数(上年同月=100)
     * sort : 1
     */

    private List<Nodes> nodes;

    public boolean issj() {
        return issj;
    }

    public void setIssj(boolean issj) {
        this.issj = issj;
    }

    public String getSelcode() {
        return selcode;
    }

    public void setSelcode(String selcode) {
        this.selcode = selcode;
    }

    public String getWdcode() {
        return wdcode;
    }

    public void setWdcode(String wdcode) {
        this.wdcode = wdcode;
    }

    public String getWdname() {
        return wdname;
    }

    public void setWdname(String wdname) {
        this.wdname = wdname;
    }

    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }

    public static class Nodes {
        private String code;
        private String name;
        private String sort;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        @Override
        public String toString() {
            return "Nodes{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", sort='" + sort + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OtherWds{" +
                "issj=" + issj +
                ", selcode='" + selcode + '\'' +
                ", wdcode='" + wdcode + '\'' +
                ", wdname='" + wdname + '\'' +
                ", nodes=" + nodes +
                '}';
    }
}
