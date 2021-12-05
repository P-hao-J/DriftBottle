package com.example.driftbottle.net;

public class BottleMessage {

    private int code = 0;
    private String img_base64;
    private String img_type;
    private String message;
    private int user_id;
    private String user_name;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getImg_base64() {
        return img_base64;
    }

    public void setImg_base64(String img_base64) {
        this.img_base64 = img_base64;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
