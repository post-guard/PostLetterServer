package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Session;
import top.rrricardo.postletterserver.services.SessionService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController extends ControllerBase {
    private final SessionMapper sessionMapper;
    private final SessionService sessionService;

    public SessionController(SessionMapper sessionMapper,
                             SessionService sessionService) {
        this.sessionMapper = sessionMapper;
        this.sessionService = sessionService;
    }

    @GetMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Session>>> getSessions() {
        return ok(sessionMapper.getSessions());
    }

    @GetMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Session>> getSessionsById(@PathVariable int id) {
        var session = sessionMapper.getSessionById(id);

        if (session == null) {
            return notFound("请求的会话不存在");
        }

        return ok(session);
    }

    @PutMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Session>> updateSession(
            @PathVariable int id,
            @RequestBody Session session) {
        if (id != session.getId()) {
            return badRequest();
        }

        var oldSession = sessionMapper.getSessionById(id);
        if (oldSession == null) {
            return notFound();
        }

        sessionMapper.updateSession(session);

        return ok(session);
    }

    @DeleteMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Session>> deleteSession(@PathVariable int id) {
        var session = sessionMapper.getSessionById(id);

        if (session == null) {
            return notFound();
        }

        sessionMapper.deleteSession(id);

        return noContent();
    }
}
