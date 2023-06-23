package top.rrricardo.postletterserver.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import top.rrricardo.postletterserver.exceptions.DeviceException;
import top.rrricardo.postletterserver.models.HeartBeat;
import top.rrricardo.postletterserver.services.HeartbeatService;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/heartbeat/{userId}")
public class HeartBeatWebsocketServer {
    private final HeartbeatService heartbeatService;
    private final static Logger logger = LoggerFactory.getLogger(MessageWebsocketServer.class);
    private final static ConcurrentHashMap<Integer, Session> sessionMap = new ConcurrentHashMap<>();
    private final static ObjectMapper s_mapper = new ObjectMapper();

    public HeartBeatWebsocketServer(HeartbeatService heartbeatService) {
        this.heartbeatService = heartbeatService;
    }

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
        logger.info("收到用户{}的心跳包", id);

        try {
            var heartbeat = s_mapper.readValue(message, HeartBeat.class);
            heartbeatService.receiveHeartBeat(heartbeat);
        } catch (JsonProcessingException e) {
            logger.warn("解析用户{}发送的心跳包：{}错误", id, message, e);
        } catch (DeviceException exception) {
            logger.info("用户{}在新设备{}上登录", id, exception.getHostname());
        }
    }

}
