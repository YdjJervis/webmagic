package us.codecraft.webmagic.netsense.stats.gov.pojo;

import java.util.List;

public class QueryData {

    private String url;
    /**
     * code : zb.A130C01_sj.201607
     * data : {"data":191495,"dotcount":2,"hasdata":true,"strdata":"191495.00"}
     * wds : [{"valuecode":"A130C01","wdcode":"zb"},{"valuecode":"201607","wdcode":"sj"}]
     */

    private List<Datanodes> datanodes;
    /**
     * nodes : [{"cname":"民间固定资产投资_累计值","code":"A130C01","dotcount":2,"exp":"","ifshowcode":false,"memo":"","name":"民间固定资产投资_累计值","nodesort":"1","sortcode":9326,"tag":"","unit":"亿元"},{"cname":"民间固定资产投资_累计增长","code":"A130C02","dotcount":1,"exp":"","ifshowcode":false,"memo":"","name":"民间固定资产投资_累计增长","nodesort":"1","sortcode":9327,"tag":"","unit":"%"},{"cname":"第一产业民间固定资产投资_累计值","code":"A130C03","dotcount":2,"exp":"","ifshowcode":false,"memo":"","name":"第一产业民间固定资产投资_累计值","nodesort":"1","sortcode":9328,"tag":"","unit":"亿元"},{"cname":"第一产业民间固定资产投资_累计增长","code":"A130C04","dotcount":1,"exp":"","ifshowcode":false,"memo":"","name":"第一产业民间固定资产投资_累计增长","nodesort":"1","sortcode":9329,"tag":"","unit":"%"},{"cname":"第二产业民间固定资产投资_累计值","code":"A130C05","dotcount":2,"exp":"","ifshowcode":false,"memo":"","name":"第二产业民间固定资产投资_累计值","nodesort":"1","sortcode":9330,"tag":"","unit":"亿元"},{"cname":"第二产业民间固定资产投资_累计增长","code":"A130C06","dotcount":1,"exp":"","ifshowcode":false,"memo":"","name":"第二产业民间固定资产投资_累计增长","nodesort":"1","sortcode":9331,"tag":"","unit":"%"},{"cname":"第三产业民间固定资产投资_累计值","code":"A130C07","dotcount":2,"exp":"","ifshowcode":false,"memo":"","name":"第三产业民间固定资产投资_累计值","nodesort":"1","sortcode":9332,"tag":"","unit":"亿元"},{"cname":"第三产业民间固定资产投资_累计增长","code":"A130C08","dotcount":1,"exp":"","ifshowcode":false,"memo":"","name":"第三产业民间固定资产投资_累计增长","nodesort":"1","sortcode":9333,"tag":"","unit":"%"}]
     * wdcode : zb
     * wdname : 指标
     */

    private List<Wdnodes> wdnodes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Datanodes> getDatanodes() {
        return datanodes;
    }

    public void setDatanodes(List<Datanodes> datanodes) {
        this.datanodes = datanodes;
    }

    public List<Wdnodes> getWdnodes() {
        return wdnodes;
    }

    public void setWdnodes(List<Wdnodes> wdnodes) {
        this.wdnodes = wdnodes;
    }

    public static class Datanodes {
        private String code;
        /**
         * data : 191495
         * dotcount : 2
         * hasdata : true
         * strdata : 191495.00
         */

        private Data data;
        /**
         * valuecode : A130C01
         * wdcode : zb
         */

        private List<Wds> wds;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public List<Wds> getWds() {
            return wds;
        }

        public void setWds(List<Wds> wds) {
            this.wds = wds;
        }

        @Override
        public String toString() {
            return "Datanodes{" +
                    "code='" + code + '\'' +
                    ", data=" + data +
                    ", wds=" + wds +
                    '}';
        }

        public static class Data {
            private String data;
            private String dotcount;
            private boolean hasdata;
            private String strdata;

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public String getDotcount() {
                return dotcount;
            }

            public void setDotcount(String dotcount) {
                this.dotcount = dotcount;
            }

            public boolean isHasdata() {
                return hasdata;
            }

            public void setHasdata(boolean hasdata) {
                this.hasdata = hasdata;
            }

            public String getStrdata() {
                return strdata;
            }

            public void setStrdata(String strdata) {
                this.strdata = strdata;
            }

            @Override
            public String toString() {
                return "Data{" +
                        "data=" + data +
                        ", dotcount=" + dotcount +
                        ", hasdata=" + hasdata +
                        ", strdata='" + strdata + '\'' +
                        '}';
            }
        }

        public static class Wds {
            private String valuecode;
            private String wdcode;

            public String getValuecode() {
                return valuecode;
            }

            public void setValuecode(String valuecode) {
                this.valuecode = valuecode;
            }

            public String getWdcode() {
                return wdcode;
            }

            public void setWdcode(String wdcode) {
                this.wdcode = wdcode;
            }

            @Override
            public String toString() {
                return "Wds{" +
                        "valuecode='" + valuecode + '\'' +
                        ", wdcode='" + wdcode + '\'' +
                        '}';
            }
        }
    }

    public static class Wdnodes {
        private String wdcode;
        private String wdname;
        /**
         * cname : 民间固定资产投资_累计值
         * code : A130C01
         * dotcount : 2
         * exp :
         * ifshowcode : false
         * memo :
         * name : 民间固定资产投资_累计值
         * nodesort : 1
         * sortcode : 9326
         * tag :
         * unit : 亿元
         */

        private List<Nodes> nodes;

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

        @Override
        public String toString() {
            return "Wdnodes{" +
                    "wdcode='" + wdcode + '\'' +
                    ", wdname='" + wdname + '\'' +
                    ", nodes=" + nodes +
                    '}';
        }

        public static class Nodes {
            private String cname;
            private String code;
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

            @Override
            public String toString() {
                return "Nodes{" +
                        "cname='" + cname + '\'' +
                        ", code='" + code + '\'' +
                        ", dotcount=" + dotcount +
                        ", exp='" + exp + '\'' +
                        ", ifshowcode=" + ifshowcode +
                        ", memo='" + memo + '\'' +
                        ", name='" + name + '\'' +
                        ", nodesort='" + nodesort + '\'' +
                        ", sortcode=" + sortcode +
                        ", tag='" + tag + '\'' +
                        ", unit='" + unit + '\'' +
                        '}';
            }
        }

    }

    @Override
    public String toString() {
        return "QueryData{" +
                "url='" + url + '\'' +
                ", datanodes=" + datanodes +
                ", wdnodes=" + wdnodes +
                '}';
    }
}
