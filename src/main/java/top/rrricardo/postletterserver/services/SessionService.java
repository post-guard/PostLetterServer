package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.mappers.MessageMapper;
import top.rrricardo.postletterserver.mappers.ParticipantMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Participant;
import top.rrricardo.postletterserver.models.Session;
import top.rrricardo.postletterserver.models.User;

import java.util.List;

@Service
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ParticipantMapper participantMapper;
    private final MessageMapper messageMapper;

    public SessionService(
            SessionMapper sessionMapper,
            ParticipantMapper participantMapper,
            MessageMapper messageMapper) {
        this.sessionMapper = sessionMapper;
        this.participantMapper = participantMapper;
        this.messageMapper = messageMapper;
    }

    /**
     * 创建好友对话对象
     * @param user 当前用户
     * @param friend 好友用户
     * @return 会话对象
     */
    @NotNull
    public Session createFriendSession(@NotNull User user, @NotNull User friend) {
        var session = new Session(user.getNickname() + "-" + friend.getNickname(), "", 2);

        sessionMapper.createSession(session);
        participantMapper.createParticipant(new Participant(user.getId(), session.getId(), 2));
        participantMapper.createParticipant(new Participant(friend.getId(), session.getId(), 2));

        return session;
    }

    /**
     * 创建群组会话对象
     * @param name 群组名称
     * @param details 群组详情
     * @param creator 创建群组者
     * @param people 群组中的用户列表
     * @return 会话对象
     */
    @NotNull
    public Session createGroupSession(
            @NotNull String name,
            @NotNull String details,
            @NotNull User creator,
            @NotNull List<User> people) {
        var session = new Session(name, details, 100);

        sessionMapper.createSession(session);
        participantMapper.createParticipant(new Participant(creator.getId(), session.getId(), 2));
        for (var user : people) {
            participantMapper.createParticipant(new Participant(user.getId(), session.getId(), 0));
        }

        return session;
    }

    /**
     * 删除会话
     * 包括消息记录
     * @param sessionId 会话对象ID
     * @throws IllegalArgumentException 欲删除的会话不存在
     */
    public void destroySession(int sessionId) throws IllegalArgumentException {
        // 确认要摧毁的会话是否存在
        var session = sessionMapper.getSessionById(sessionId);
        if (session == null) {
            throw new IllegalArgumentException("欲删除的会话不存在");
        }

        // 删除消息记录
        var messages = messageMapper.getMessagesBySessionId(session.getId());
        for (var message : messages) {
            messageMapper.deleteMessage(message.getId());
        }

        // 删除参与者
        var participants = participantMapper.getParticipantsBySessionId(session.getId());
        for (var participant : participants) {
            participantMapper.deleteParticipant(participant.getId());
        }

        // 删除会话本身
        sessionMapper.deleteSession(session.getId());
    }
}
