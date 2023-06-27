package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.mappers.ParticipantMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.Participant;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController extends ControllerBase {
    private final ParticipantMapper participantMapper;
    private final UserMapper userMapper;
    private final SessionMapper sessionMapper;

    public ParticipantController(
            ParticipantMapper participantMapper,
            UserMapper userMapper,
            SessionMapper sessionMapper) {
        this.participantMapper = participantMapper;
        this.userMapper = userMapper;
        this.sessionMapper = sessionMapper;
    }

    @GetMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Participant>>> getParticipants() {
        return ok(participantMapper.getParticipants());
    }

    @GetMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Participant>> getParticipantById(@PathVariable int id) {
        var participant = participantMapper.getParticipantById(id);

        if (participant == null) {
            return notFound();
        }

        return ok(participant);
    }

    @GetMapping("/user/{userId}")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Participant>>> getParticipantsByUserId(
            @PathVariable int userId) {
        var user = userMapper.getUserById(userId);

        if (user == null) {
            return notFound("查询的用户不存在");
        }

        return ok(participantMapper.getParticipantsByUserId(userId));
    }

    @GetMapping("/session/{sessionId}")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Participant>>> getParticipantsBySessionId(
            @PathVariable int sessionId) {
        var session = sessionMapper.getSessionById(sessionId);

        if (session == null) {
            return notFound("查询的会话不存在");
        }

        return ok(participantMapper.getParticipantsBySessionId(sessionId));
    }

    @PostMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<Participant>> createParticipant(@RequestBody Participant participant) {
        participantMapper.createParticipant(participant);

        return ok(participant);
    }

    @PutMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Participant>> updateParticipant(
            @PathVariable int id,
            @RequestBody Participant participant
    ) {
        if (id != participant.getId()) {
            return badRequest();
        }

        var oldParticipant = participantMapper.getParticipantById(id);
        if (oldParticipant == null) {
            return notFound();
        }

        participantMapper.updateParticipant(participant);

        return ok(participant);
    }

    @DeleteMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Participant>> deleteParticipant(@PathVariable int id) {
        var participant = participantMapper.getParticipantById(id);

        if (participant == null) {
            return notFound();
        }

        participantMapper.deleteParticipant(id);

        return ok("删除成功", participant);
    }
}
