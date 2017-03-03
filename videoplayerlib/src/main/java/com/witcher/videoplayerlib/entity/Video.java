package com.witcher.videoplayerlib.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class Video implements Parcelable {
    private String name;

    private boolean isLive;

    private List<Definition> list;

    public Video() {
    }

    public Video(String name, List<Definition> list) {
        this.name = name;
        this.list = list;
    }

    public List<Definition> getList() {
        return list;
    }

    public void setList(List<Definition> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    @Override
    public int describeContents() {
        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(this.isLive ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.list);
    }

    protected Video(Parcel in) {
        this.name = in.readString();
        this.isLive = in.readByte() != 0;
        this.list = in.createTypedArrayList(Definition.CREATOR);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
