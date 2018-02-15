package com.dmb.testriotapi.Models;

/**
 * Created by davidmari on 15/2/18.
 */

public class Skin {

    private String name,number;

    public Skin(){

    }

    public Skin(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
