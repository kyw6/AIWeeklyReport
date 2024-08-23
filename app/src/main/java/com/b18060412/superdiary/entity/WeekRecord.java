package com.b18060412.superdiary.entity;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class WeekRecord {

    @SerializedName("WrID")
    private String wrid;
    @SerializedName("Content")
    private String content;
    @SerializedName("Mind")
    private String mind;

    public String getWrid() {
        return wrid;
    }

    public void setWrid(String wrid) {
        this.wrid = wrid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMind() {
        return mind;
    }

    public void setMind(String mind) {
        this.mind = mind;
    }
}
