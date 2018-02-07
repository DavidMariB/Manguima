package com.dmb.testriotapi.Models;

/**
 * Created by davidmari on 6/2/18.
 */

public class Match {

    private String champImg,score,result,champName,lane;

    public Match(String ci, String s, String r, String cn,String l){

        this.champImg = ci;
        this.score = s;
        this.result = r;
        this.champName = cn;
        this.lane = l;
    }

    public String getChampImg() {
        return champImg;
    }

    public void setChampImg(String champImg) {
        this.champImg = champImg;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getChampName() {
        return champName;
    }

    public void setChampName(String champName) {
        this.champName = champName;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

}
