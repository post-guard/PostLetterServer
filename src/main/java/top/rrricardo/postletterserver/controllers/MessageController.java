package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.exceptions.SendMessageException;
import top.rrricardo.postletterserver.mappers.MessageMapper;
import top.rrricardo.postletterserver.mappers.SessionMapper;
import top.rrricardo.postletterserver.models.Message;
import top.rrricardo.postletterserver.services.MessageService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController extends ControllerBase {
    private final MessageMapper messageMapper;
    private final SessionMapper sessionMapper;
    private final MessageService messageService;

    public MessageController(MessageMapper messageMapper,
                             SessionMapper sessionMapper,
                             MessageService messageService) {
        this.messageMapper = messageMapper;
        this.sessionMapper = sessionMapper;
        this.messageService = messageService;
    }

    @GetMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Message>>> getMessages() {
        return ok(messageMapper.getMessages());
    }

    @GetMapping("/session/{sessionId}")
    @Authorize
    public ResponseEntity<ResponseDTO<List<Message>>> getMessagesBySessionId(@PathVariable int sessionId) {
        var session = sessionMapper.getSessionById(sessionId);

        if (session == null) {
            return notFound("查询的会话不存在");
        }

        return ok(messageMapper.getMessagesBySessionId(sessionId));
    }

    @PostMapping("/send")
    @Authorize
    public ResponseEntity<ResponseDTO<Message>> sendMessage(@RequestBody Message message) {
        try {
            messageService.sendMessage(message);

            return ok();
        } catch (SendMessageException exception) {
            return badRequest(exception.getMessage());
        }
    }
}
