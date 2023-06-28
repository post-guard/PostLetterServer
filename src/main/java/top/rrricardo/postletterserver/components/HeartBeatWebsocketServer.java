package top.rrricardo.postletterserver.components;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/heartbeat/{userId}")
public class HeartBeatWebsocketServer {
    private final static Logger logger = LoggerFactory.getLogger(MessageWebsocketServer.class);
    private final static ConcurrentHashMap<Integer, Session> sessionMap = new ConcurrentHashMap<>();
    public final static ConcurrentHashMap<Integer, LocalDateTime> s_heartBeatMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int id) {
        sessionMap.put(id, session);
        logger.info("用户{}开始发送心跳包", id);
    }

    @OnError
    public void onError(Session session, Throwable exception, @PathParam("userId") int id) {
        logger.error("用户{}发送心跳包时发生错误", id, exception);
        sessionMap.remove(id);

        try {
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            logger.error("关闭用户{}心跳包发送通道时失败", id, e);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") int id) {
        sessionMap.remove(id);

        logger.info("关闭用户{}心跳包发送通道", id);

        try {
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            logger.error("关闭用户{}心跳包发送通道时失败", id, e);
        }
    }

    @OnMessage
    public void onMessage(Session session, String message, @PathParam("userId") int id) {
        if (message.equals("ping")) {
            logger.info("用户{}心跳", id);
            s_heartBeatMap.put(id, LocalDateTime.now());
        } else {
            logger.warn("收到用户{}的非法信息：{}", id, message);
        }
    }

}
