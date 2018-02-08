package com.dmb.testriotapi.Models;

/**
 * Created by davidmari on 6/2/18.
 */

public class Match {

    private String champImg,lane,champName,result,kills,deaths,assists;

    public Match(String champImg,String lane,String champName,String result,String kills,String deaths,String assists){

        this.champImg = champImg;
        this.lane = lane;
        this.champName = champName;
        this.result = result;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;

    }

    public String getChampImg() {
        return champImg;
    }

    public void setChampImg(String champImg) {
        this.champImg = champImg;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public String getChampName() {
        return champName;
    }

    public void setChampName(String champName) {
        this.champName = champName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getKills() {
        return kills;
    }

    public void setKills(String kills) {
        this.kills = kills;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }
}
