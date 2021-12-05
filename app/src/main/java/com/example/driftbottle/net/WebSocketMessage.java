package com.example.driftbottle.net;

public class WebSocketMessage {

    public static final int OPEN = 0;
    public static final int CLOSE = 1;
    public static final int ERROR = 2;
    public static final int MESSAGE = 3;
    private String msg = null;
    private int code;

    public WebSocketMessage(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public WebSocketMessage() {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
