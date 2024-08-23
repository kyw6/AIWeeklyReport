package com.b18060412.superdiary.entity;

import java.util.List;

public class Mind {

    private String content;
    private List<Mind> sub;

    public Mind(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Mind> getSub() {
        return sub;
    }

    public void setSub(List<Mind> sub) {
        this.sub = sub;
    }
}
