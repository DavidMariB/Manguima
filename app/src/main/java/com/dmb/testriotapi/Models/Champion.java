package com.dmb.testriotapi.Models;

/**
 * Created by davidmari on 4/2/18.
 */

public class Champion {

    String name;
    String key;
    String image;

    public Champion(String name, String key, String image){
        this.name = name;
        this.key = key;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
