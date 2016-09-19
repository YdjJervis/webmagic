package us.codecraft.webmagic.samples.amazon.pojo;

/**
 * 评论
 */
public class Discuss {

    private String asin;
    private String title;
    private String time;
    private String content;
    private String version;
    private String person;
    private String buyStatus;
    private String star;

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getAsin() {
        return asin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getBuyStatus() {
        return buyStatus;
    }

    public void setBuyStatus(String buyStatus) {
        this.buyStatus = buyStatus;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "asin='" + asin + '\'' +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", version='" + version + '\'' +
                ", person='" + person + '\'' +
                ", buyStatus='" + buyStatus + '\'' +
                ", star=" + star +
                '}';
    }
}
