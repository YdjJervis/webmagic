package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * @author Jervis
 * @version V0.1
 * @Description: 验证码识别结果
 * @date 2016/10/19 17:53
 */
public class ImgValidateResult {

    private boolean RequestSuccess;
    private boolean Identification;
    private String Value;
    private String Message;
    private String RawUrl;

    public boolean isRequestSuccess() {
        return RequestSuccess;
    }

    public void setRequestSuccess(boolean RequestSuccess) {
        this.RequestSuccess = RequestSuccess;
    }

    public boolean isIdentification() {
        return Identification;
    }

    public void setIdentification(boolean Identification) {
        this.Identification = Identification;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getRawUrl() {
        return RawUrl;
    }

    public void setRawUrl(String RawUrl) {
        this.RawUrl = RawUrl;
    }

    @Override
    public String toString() {
        return "ImgValidateResult{" +
                "RequestSuccess=" + RequestSuccess +
                ", Identification=" + Identification +
                ", Value='" + Value + '\'' +
                ", Message='" + Message + '\'' +
                ", RawUrl='" + RawUrl + '\'' +
                '}';
    }
}