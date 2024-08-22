package com.b18060412.superdiary.network.responses;

public class DiaryResponse {
    private String Date;
    private String Content;


    //服务端返回的date："Date": "2024-08-14T00:00:00+08:00"
    // Getters and Setters
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    @Override
    public String toString() {
        return "DataItem{" +
                "Date='" + Date + '\'' +
                ", Content='" + Content + '\'' +
                '}';
    }
}
