package com.yunda.base.system.vo;

/**
 * @author beidouxing
 * @create 2019/04/16 19:59
 */
public class Data {
    /*图片名*/
    private String alt="";
    /*图片id*/
    private int pid;
    /*原图地址*/
    private String src="";
    /*缩略图地址*/
    private String thumb="";

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "Data{" +
                "alt='" + alt + '\'' +
                ", pid='" + pid + '\'' +
                ", src='" + src + '\'' +
                ", thumb='" + thumb + '\'' +
                '}';
    }
}
