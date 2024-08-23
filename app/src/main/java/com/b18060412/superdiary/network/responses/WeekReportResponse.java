package com.b18060412.superdiary.network.responses;

public class WeekReportResponse {
    private int WrID;
    private String Content;
    private String Mind;

    // Getters and Setters
    public int getWrID() {
        return WrID;
    }

    public void setWrID(int wrID) {
        WrID = wrID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMind() {
        return Mind;
    }

    public void setMind(String mind) {
        Mind = mind;
    }
}
