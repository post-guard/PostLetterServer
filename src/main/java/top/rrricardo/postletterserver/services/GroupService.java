package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.dtos.GroupDTO;
import top.rrricardo.postletterserver.mappers.ParticipantMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.Session;
import top.rrricardo.postletterserver.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final ParticipantMapper participantMapper;
    private final SessionMapper sessionMapper;
    private final UserMapper userMapper;
    private final SessionService sessionService;

    public GroupService(
            ParticipantMapper participantMapper,
            SessionMapper sessionMapper,
            UserMapper userMapper,
            SessionService sessionService) {
        this.participantMapper = participantMapper;
        this.sessionMapper = sessionMapper;
        this.userMapper = userMapper;
        this.sessionService = sessionService;
    }

    @NotNull
    public List<GroupDTO> queryUserGroup(int userId) {
        var participants = participantMapper.getParticipantsByUserId(userId);
        List<Session> sessions = new ArrayList<>();

        for (var participant : participants) {
            var session = sessionMapper.getSessionById(participant.getSessionId());

            if (session != null) {
                if (session.getLevel() > 2) {
                    sessions.add(session);
                }
            }
        }

        List<GroupDTO> groups = new ArrayList<>();

        for (var session : sessions) {
            var people = participantMapper.getParticipantsBySessionId(session.getId());
            var users = new ArrayList<Integer>();

            for (var p : people) {
                users.add(p.getUserId());
            }

            groups.add(new GroupDTO(session.getName(), session.getDetails(), session.getId(), users));
        }

        return groups;
    }

    public void createGroup(int creatorId, @NotNull GroupDTO groupDTO) {
        var creator = userMapper.getUserById(creatorId);

        if (creator == null) {
            throw new IllegalArgumentException("创建者不存在");
        }

        var users = new ArrayList<User>();

        for (var i : groupDTO.getParticipants()) {
            var user = userMapper.getUserById(i);

            if (user == null) {
                throw new IllegalArgumentException("指定的群成员不存在");
            }

            users.add(user);
        }

        sessionService.createGroupSession(groupDTO.getName(), groupDTO.getDetails(), creator, users);
    }

    public void deleteGroup(int operatorId, @NotNull GroupDTO groupDTO) {
        var oldGroup = sessionMapper.getSessionById(groupDTO.getSessionId());

        if (oldGroup == null) {
            throw new IllegalArgumentException("欲删除的群组不存在");
        }
        
        var participants = participantMapper.getParticipantsBySessionId(groupDTO.getSessionId());

        var flag = true;
        for (var participant : participants) {
            if (participant.getUserId() == operatorId) {
                if (participant.getPermission() == 2) {
                    flag = false;
                }
            }
        }

        if (flag) {
            throw new IllegalArgumentException("不在群组中或者没有权限");
        }

        sessionService.destroySession(groupDTO.getSessionId());
    }
}
