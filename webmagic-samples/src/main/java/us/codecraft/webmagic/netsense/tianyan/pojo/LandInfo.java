package us.codecraft.webmagic.netsense.tianyan.pojo;

/**
 * 土地信息
 */
public class LandInfo{

    //SELECT buyuser,`owner`,TRANSFEROR FROM t_ori_landinfo LIMIT 100
    private String buyuser;
    private String owner;
    private String TRANSFEROR;

    public String getBuyuser() {
        return buyuser;
    }

    public void setBuyuser(String buyuser) {
        this.buyuser = buyuser;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTRANSFEROR() {
        return TRANSFEROR;
    }

    public void setTRANSFEROR(String TRANSFEROR) {
        this.TRANSFEROR = TRANSFEROR;
    }

    @Override
    public String toString() {
        return "LandInfo{" +
                "buyuser='" + buyuser + '\'' +
                ", owner='" + owner + '\'' +
                ", TRANSFEROR='" + TRANSFEROR + '\'' +
                '}';
    }
}
