package com.dmb.testriotapi.Models.Forum;

/**
 * Created by Francisco on 15/02/2018.
 */

public class Chat {

    private String uid1, uid2, key;

    public Chat () {}

    public Chat(String uid1, String uid2, String key) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.key = key;
    }

    public String getUid1() {
        return uid1;
    }

    public void setUid1(String uid1) {
        this.uid1 = uid1;
    }

    public String getUid2() {
        return uid2;
    }

    public void setUid2(String uid2) {
        this.uid2 = uid2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
