package com.b18060412.superdiary.network.responses;

import java.util.List;


public class DiaryResponse {
    private int code;
    private List<DataItem> data;
    private String msg;

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}

class DataItem {
    private String Date;
    private String Content;

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
