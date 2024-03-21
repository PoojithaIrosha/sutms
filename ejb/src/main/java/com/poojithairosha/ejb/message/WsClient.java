package com.poojithairosha.ejb.message;

import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class WsClient {

    private Session session;

    public WsClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8080/sutms/device-data"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from server: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        this.session = null;
        System.out.println("Connection closed");
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
