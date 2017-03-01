package com.witcher.videoplayerlib.entity;

import java.util.List;

/**
 * Created by witcher on 2017/2/27 0027.
 */

public class Video {
    private String name;

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

}
