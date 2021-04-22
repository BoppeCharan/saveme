package com.example.saveme;

public class Info {
    String key;
    String info;

    public Info(){
    }

    public Info(String key, String info) {
        this.key = key;
        this.info = info;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
