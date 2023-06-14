package top.rrricardo.postletterserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.components.MessageWebsocketServer;
import top.rrricardo.postletterserver.exceptions.SendMessageException;
import top.rrricardo.postletterserver.mappers.MessageMapper;
import top.rrricardo.postletterserver.mappers.ParticipantMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Message;

@Service
public class MessageService {
    private final MessageMapper messageMapper;
    private final ParticipantMapper participantMapper;
    private final SessionMapper sessionMapper;
    private final ObjectMapper objectMapper;

    public MessageService(MessageMapper messageMapper,
                          ParticipantMapper participantMapper,
                          SessionMapper sessionMapper) {
        this.messageMapper = messageMapper;
        this.participantMapper = participantMapper;
        this.sessionMapper = sessionMapper;

        objectMapper = new ObjectMapper();
    }

    /**
     * 发送消息
     * @param message 需要发送的消息
     * @throws SendMessageException 发送消息失败的异常
     */
    public void sendMessage(@NotNull Message message) throws SendMessageException {
        var session = sessionMapper.getSessionById(message.getSessionId());

        if (session == null) {
            throw new SendMessageException(message, "会话不存在");
        }

        var participants = participantMapper.getParticipantsBySessionId(message.getSessionId());

        var flag = false;
        for (var participant : participants) {
            if (participant.getUserId() == message.getSendId()) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw new SendMessageException(message, "用户不在会话中");
        }

        messageMapper.createMessage(message);

        try {
            var text = objectMapper.writeValueAsString(message);

            for (var participant : participants) {
                MessageWebsocketServer.sendMessage(text, participant.getUserId());
            }
        } catch (JsonProcessingException e) {
            throw new SendMessageException(message, "JSON格式化异常", e);
        }
    }
}
