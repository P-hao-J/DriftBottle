package com.example.driftbottle.net;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class JWebSocketClient extends WebSocketClient {

    private static final String TAG = "JWebSocketClient";
    private final static String host = "ws://119.91.138.130:8080/ws";
    private static URI uri;


    public static JWebSocketClient getInstance(){
        return WebSocketClientHolder.sInstance;
    }

    private static class WebSocketClientHolder{
        static {
            try {
                uri = new URI(host);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        private static final JWebSocketClient sInstance = new JWebSocketClient(uri);
    }

    public JWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public JWebSocketClient(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    public JWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    public JWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders) {
        super(serverUri, protocolDraft, httpHeaders);
    }

    public JWebSocketClient(URI serverUri, Draft protocolDraft, Map<String, String> httpHeaders, int connectTimeout) {
        super(serverUri, protocolDraft, httpHeaders, connectTimeout);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d(TAG, "=== onOpen: status--> "+handshakedata.getHttpStatus());
        Log.d(TAG, "=== onOpen: message--> "+handshakedata.getHttpStatusMessage());
        WebSocketMessage message = new WebSocketMessage();
        message.setCode(WebSocketMessage.OPEN);
        EventBus.getDefault().post(message);
    }

    @Override
    public void onMessage(String message) {
        WebSocketMessage object = new WebSocketMessage();
        object.setCode(WebSocketMessage.MESSAGE);
        object.setMsg(message);
        EventBus.getDefault().post(object);
        Log.d(TAG, "=== onMessage: ");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        WebSocketMessage message = new WebSocketMessage();
        message.setCode(WebSocketMessage.CLOSE);
        EventBus.getDefault().post(message);
        Log.d(TAG, "=== onClose: code--> "+code);
        Log.d(TAG, "=== onClose: reason--> "+reason);
        Log.d(TAG, "=== onClose: remote--> "+remote);
    }

    @Override
    public void onError(Exception ex) {
        WebSocketMessage message = new WebSocketMessage();
        message.setCode(WebSocketMessage.ERROR);
        EventBus.getDefault().post(message);
        Log.d(TAG, "=== onError: ");
    }
}
