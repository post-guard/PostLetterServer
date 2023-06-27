package top.rrricardo.postletterserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.annotations.Authorize;
import top.rrricardo.postletterserver.dtos.LoginDTO;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.dtos.UserDTO;
import top.rrricardo.postletterserver.exceptions.LoginException;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.User;
import top.rrricardo.postletterserver.services.HeartbeatService;
import top.rrricardo.postletterserver.services.JwtService;
import top.rrricardo.postletterserver.services.UserService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final HeartbeatService heartbeatService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(
            UserMapper userMapper,
            UserService userService,
            JwtService jwtService,
            HeartbeatService heartbeatService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.jwtService = jwtService;
        this.heartbeatService = heartbeatService;
    }

    @GetMapping("/")
    @Authorize
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers() {
        var users = userMapper.getUsers();

        var result = new ArrayList<UserDTO>();

        for (var user : users) {
            result.add(new UserDTO(user));
        }

        return ok(result);
    }

    @GetMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable int id) {
        var user = userMapper.getUserById(id);

        if (user == null) {
            return notFound();
        }

        return ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<UserDTO>> updateUser(
            @PathVariable int id,
            @RequestBody User user) {
        if (user.getId() != id) {
            return badRequest("请求路径和请求提不符合");
        }

        var oldUser = userMapper.getUserById(id);

        if (oldUser == null) {
            return notFound();
        }

        userMapper.updateUser(user);

        return ok("修改用户请求成功", new UserDTO(user));
    }

    @DeleteMapping("/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<UserDTO>> deleteUser(@PathVariable int id) {
        var oldUser = userMapper.getUserById(id);

        if (oldUser == null) {
            return notFound();
        }

        userMapper.deleteUser(id);

        return noContent();
    }

    @GetMapping("/online/{id}")
    @Authorize
    public ResponseEntity<ResponseDTO<Boolean>> queryOnline(@PathVariable int id) {
        var user = userMapper.getUserById(id);

        if (user == null) {
            return notFound("查询的用户不存在");
        }

        return ok(heartbeatService.queryOnlineState(id));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserDTO>> register(@RequestBody User user) {
        try {
            userService.register(user);

            return created(new UserDTO(user));
        } catch (IllegalArgumentException e) {
            return badRequest(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<HashMap<String, Object>>> login(@RequestBody LoginDTO loginDTO) {
        try {
            var user = userService.login(loginDTO);

            heartbeatService.setHostname(user.getId(), loginDTO.getHostname());

            logger.info("用户{}在设备{}上登录", loginDTO.getUsername(), loginDTO.getHostname());
            var map = new HashMap<String, Object>();
            map.put("id", user.getId());
            map.put("nickname", user.getNickname());
            map.put("username", user.getUsername());
            map.put("token", jwtService.generateJwtToken(user, loginDTO.getHostname()));

            return ok(map);
        } catch (LoginException exception) {
            return badRequest(exception.getMessage());
        }
    }
}
