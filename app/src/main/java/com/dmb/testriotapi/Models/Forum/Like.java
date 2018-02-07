package com.dmb.testriotapi.Models.Forum;

/**
 * Created by Ricardo Borrull on 07/02/2018.
 */

public class Like {

    private String uid;

    public Like() {

    }

    public Like(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Like{" +
                "uid='" + uid + '\'' +
                '}';
    }
}
