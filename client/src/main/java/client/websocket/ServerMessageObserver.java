package client.websocket;

import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;

public interface ServerMessageObserver {
    void notify(ServerMessage message);
}
