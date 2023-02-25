package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private String imgurl;
    private Timestamp timestamp;
    private String userid;
    private String username;

    public Journal() {
    }

    public Journal(String title, String thought, String imgurl, Timestamp timestamp, String userid, String username) {
        this.title = title;
        this.thought = thought;
        this.imgurl = imgurl;
        this.timestamp = timestamp;
        this.userid = userid;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
