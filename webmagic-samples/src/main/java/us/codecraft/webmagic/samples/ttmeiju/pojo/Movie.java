package us.codecraft.webmagic.samples.ttmeiju.pojo;

import org.hibernate.annotations.Type;
import us.codecraft.webmagic.samples.base.entity.BasePojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Movie extends BasePojo {

    private String seri;//网页上的排序
    private String name;
    private String download;//用json封装的多个下载地址
    private String size;
    private String type;//格式
    private String words;//字幕
    private String discuss;//讨论
    private String img;
    private String introduce;

    @Basic
    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Type(type = "text")
    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    @Basic
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Basic
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Basic
    public String getDiscuss() {
        return discuss;
    }

    public void setDiscuss(String discuss) {
        this.discuss = discuss;
    }

    @Basic
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Basic
    @Type(type = "text")
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "seri='" + seri + '\'' +
                ", name='" + name + '\'' +
                ", download='" + download + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                ", words='" + words + '\'' +
                ", discuss='" + discuss + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
