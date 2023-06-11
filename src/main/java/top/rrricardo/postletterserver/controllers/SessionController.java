package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Session;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController extends ControllerBase {
    private final SessionMapper sessionMapper;

    public SessionController(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<Session>>> getSessions() {
        return ok(sessionMapper.getSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Session>> getSessionsById(@PathVariable int id) {
        var session = sessionMapper.getSessionById(id);

        if (session == null) {
            return notFound("请求的会话不存在");
        }

        return ok(session);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<Session>> createSession(@RequestBody Session session) {
        sessionMapper.createSession(session);

        return created(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Session>> updateSession(
            @PathVariable int id,
            @RequestBody Session session) {
        if (id != session.getId()) {
            return badRequest();
        }

        sessionMapper.updateSession(session);

        return ok(session);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Session>> deleteSession(@PathVariable int id) {
        var session = sessionMapper.getSessionById(id);

        if (session == null) {
            return notFound();
        }

        sessionMapper.deleteSession(id);

        return noContent();
    }
}