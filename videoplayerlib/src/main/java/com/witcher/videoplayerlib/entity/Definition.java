package com.witcher.videoplayerlib.entity;

/**
 * Created by witcher on 2017/3/1 0001.
 */

public class Definition {
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
}
