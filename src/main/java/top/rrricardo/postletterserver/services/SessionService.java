package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.dtos.CreateSessionDto;
import top.rrricardo.postletterserver.mappers.ParticipantMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Session;

@Service
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ParticipantMapper participantMapper;

    public SessionService(SessionMapper sessionMapper, ParticipantMapper participantMapper) {
        this.sessionMapper = sessionMapper;
        this.participantMapper = participantMapper;
    }

    /**
     * 创建会话
     * @param dto 创建会话对象
     */
    @NotNull
    public Session createSession(@NotNull CreateSessionDto dto) {
        var level = 0;
        if (dto.getParticipants().size() <= 2) {
            level = 2;
        } else {
            level = 100;
        }

        var session = new Session(dto.getName(), dto.getDetails(), level);

        sessionMapper.createSession(session);

        for (var participant : dto.getParticipants()) {
            participant.setSessionId(session.getId());

            participantMapper.createParticipant(participant);
        }

        return session;
    }
}
