package us.codecraft.webmagic.netsense.stats.gov.pojo;


public class Wds {


    /**
     * wdcode : zb
     * valuecode : A01010103
     */

    private String wdcode;
    private String valuecode;

    public Wds(String wdcode, String valuecode) {
        this.wdcode = wdcode;
        this.valuecode = valuecode;
    }

    public String getWdcode() {
        return wdcode;
    }

    public void setWdcode(String wdcode) {
        this.wdcode = wdcode;
    }

    public String getValuecode() {
        return valuecode;
    }

    public void setValuecode(String valuecode) {
        this.valuecode = valuecode;
    }

    @Override
    public String toString() {
        return "Wds{" +
                "wdcode='" + wdcode + '\'' +
                ", valuecode='" + valuecode + '\'' +
                '}';
    }
}
