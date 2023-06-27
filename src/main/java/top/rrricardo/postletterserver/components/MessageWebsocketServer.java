package top.rrricardo.postletterserver.components;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/message/{userId}")
public class MessageWebsocketServer {
    private final static Logger logger = LoggerFactory.getLogger(MessageWebsocketServer.class);
    private final static ConcurrentHashMap<Integer, Session> sessionMap = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Integer, LinkedList<String>> s_sendQueues = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") int id) {
        sessionMap.put(id, session);
        logger.info("用户{}开始接受消息推送", id);

        // 发送滞留在推送队列中的消息
        var queue = s_sendQueues.get(id);

        if (queue != null) {
            while (!queue.isEmpty()) {
                var message = queue.pop();
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException exception) {
                    logger.error("给用户{}推送消息失败", id, exception);
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable exception, @PathParam("userId") int id) {
        logger.error("用户{}推送消息时发生错误", id, exception);
        sessionMap.remove(id);

        try {
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            logger.error("关闭用户{}消息推送通道时失败", id, e);
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("userId") int id) {
        sessionMap.remove(id);

        logger.info("关闭用户{}消息推送通道", id);

        try {
            if (session != null) {
                session.close();
            }
        } catch (IOException e) {
            logger.error("关闭用户{}消息推送通道时失败", id, e);
        }
    }

    /**
     * 给指定用户推送消息
     *
     * @param message 需要推送的消息
     * @param userId  指定的用户
     */
    public static void sendMessage(@NotNull String message, int userId) {
        var session = sessionMap.get(userId);

        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException exception) {
                logger.error("给用户{}推送消息失败", userId, exception);
            }
        } else {
            // 当前用户不在线 存储到发送队列中
            var queue = s_sendQueues.computeIfAbsent(userId, k -> new LinkedList<>());

            queue.add(message);
        }
    }
}
