package com.b18060412.superdiary.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// 用于兼容服务端的两层思维导图结构
public class ServerMind {

    @SerializedName("Done")
    private List<String> done;
    @SerializedName("Doing")
    private List<String> doing;
    @SerializedName("Planing")
    private List<String> planing;

    public List<String> getDone() {
        return done;
    }

    public void setDone(List<String> done) {
        this.done = done;
    }

    public List<String> getDoing() {
        return doing;
    }

    public void setDoing(List<String> doing) {
        this.doing = doing;
    }

    public List<String> getPlaning() {
        return planing;
    }

    public void setPlaning(List<String> planing) {
        this.planing = planing;
    }
}
