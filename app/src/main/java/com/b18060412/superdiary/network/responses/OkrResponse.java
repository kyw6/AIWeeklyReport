package com.b18060412.superdiary.network.responses;

import java.util.List;

public class OkrResponse {

    private int code;  // 消息码
    private String msg;  // 错误提示
    private Data data;  // data 对象

    // Getters and Setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<String> okr;  // okr列表
        private List<String> krs;  // krs列表
        private String content;    // 更新字段名
        private String propose;    // 更新字段名

        // Getters and Setters

        public List<String> getOkr() {
            return okr;
        }

        public void setOkr(List<String> okr) {
            this.okr = okr;
        }

        public List<String> getKrs() {
            return krs;
        }

        public void setKrs(List<String> krs) {
            this.krs = krs;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPropose() {
            return propose;
        }

        public void setPropose(String propose) {
            this.propose = propose;
        }
    }

}
