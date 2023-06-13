package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.dtos.LoginDTO;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.dtos.UserDTO;
import top.rrricardo.postletterserver.exceptions.LoginException;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.User;
import top.rrricardo.postletterserver.services.JwtService;
import top.rrricardo.postletterserver.services.UserService;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserMapper userMapper, UserService userService, JwtService jwtService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getUsers() {
        var users = userMapper.getUsers();

        var result = new ArrayList<UserDTO>();

        for (var user : users) {
            result.add(new UserDTO(user));
        }

        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable int id) {
        var user = userMapper.getUserById(id);

        if (user == null) {
            return notFound();
        }

        return ok(new UserDTO(user));
    }

    @PutMapping("/{id}")
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
    public ResponseEntity<ResponseDTO<UserDTO>> deleteUser(@PathVariable int id) {
        var oldUser = userMapper.getUserById(id);

        if (oldUser == null) {
            return notFound();
        }

        userMapper.deleteUser(id);

        return noContent();
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<UserDTO>> register(@RequestBody User user) {
        userService.register(user);

        return created(new UserDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(@RequestBody LoginDTO loginDTO) {
        try {
            userService.login(loginDTO);

            var user = userMapper.getUserById(loginDTO.getUserId());

            if (user != null) {
                return ok("登录成功", jwtService.generateJwtToken(user));
            } else {
                return notFound("用户不存在");
            }
        } catch (LoginException exception) {
            return badRequest(exception.getMessage());
        }
    }
}
