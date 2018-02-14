package com.dmb.testriotapi.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by davidmari on 4/2/18.
 */

public class Champion implements Parcelable {

    String name;
    String key;
    String title;
    String image;

    public Champion(String name, String key,String title, String image){
        this.name = name;
        this.key = key;
        this.title = title;
        this.image = image;
    }

    protected Champion(Parcel in) {
        name = in.readString();
        key = in.readString();
        title = in.readString();
        image = in.readString();
    }

    public static final Creator<Champion> CREATOR = new Creator<Champion>() {
        @Override
        public Champion createFromParcel(Parcel in) {
            return new Champion(in);
        }

        @Override
        public Champion[] newArray(int size) {
            return new Champion[size];
        }
    };

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
        dest.writeString(title);
        dest.writeString(image);
    }
}
