package com.example.cloud_forecast_test1.db;

public class DatabaseBean {
    private  int  _id; //主键城市ID
    private  String city;  //城市名
    private  String content;  //城市相关天气信息

    public DatabaseBean() {
    }

    public DatabaseBean(int _id, String city, String content) {
        this._id = _id;
        this.city = city;
        this.content = content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
