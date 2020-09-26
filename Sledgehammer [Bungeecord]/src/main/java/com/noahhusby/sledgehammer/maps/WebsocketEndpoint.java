package com.noahhusby.sledgehammer.maps;

import java.net.URI;

import jakarta.websocket.*;

@ClientEndpoint
public class WebsocketEndpoint {

    Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketEndpoint(URI endpointURI) {
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (DeploymentException e) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}
