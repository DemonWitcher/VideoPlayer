package com.witcher.videoplayerlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class Definition implements Parcelable{
    private String name;
    private String url;

    public Definition() {
    }

    public Definition(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
    }

    protected Definition(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
    }

    public static final Creator<Definition> CREATOR = new Creator<Definition>() {
        @Override
        public Definition createFromParcel(Parcel source) {
            return new Definition(source);
        }

        @Override
        public Definition[] newArray(int size) {
            return new Definition[size];
        }
    };
}
