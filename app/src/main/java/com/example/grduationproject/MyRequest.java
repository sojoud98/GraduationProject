package com.example.grduationproject;
public class MyRequest {

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMobile() {
        return mobile;
    }

    public String getService() {
        return service;
    }


    public String getServed() {
        return served;
    }

    public void setServed(String served) {
        this.served = served;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MyRequest(long id, String info, String date, String time, String mobile, String service, String served) {
        this.info = info;
        this.date = date;
        this.time = time;
        this.id=id;
        this.mobile = mobile;
        this.service = service;
        this.served=served;
    }

    String info,date,time,mobile,service;
    String served;
    long id;
}