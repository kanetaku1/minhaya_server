package minhaya;

import org.glassfish.tyrus.server.Server;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/websocket")
public class WebSocketServer {

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("Received message: " + message);
        session.getBasicRemote().sendText("Server received your message: " + message);
    }

    public void StartServer() {
        Server server = new Server("localhost", 8080, "", null, WebSocketServer.class);
        try {
            server.start();
            System.out.println("WebSocket server started...");
            Thread.sleep(Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}