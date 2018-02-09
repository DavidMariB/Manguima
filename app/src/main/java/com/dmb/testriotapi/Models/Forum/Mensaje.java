package com.dmb.testriotapi.Models.Forum;

/**
 * Created by 2dam on 09/02/2018.
 */

public class Mensaje {

    private String text;
    private String uid;

    public Mensaje(String text, String uid) {
        this.text = text;
        this.uid = uid;
    }

    public Mensaje() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
