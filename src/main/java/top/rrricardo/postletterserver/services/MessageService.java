package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
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

    public MessageService(MessageMapper messageMapper,
                          ParticipantMapper participantMapper,
                          SessionMapper sessionMapper) {
        this.messageMapper = messageMapper;
        this.participantMapper = participantMapper;
        this.sessionMapper = sessionMapper;
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
    }
}
