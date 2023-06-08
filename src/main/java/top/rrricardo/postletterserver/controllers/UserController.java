package top.rrricardo.postletterserver.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.rrricardo.postletterserver.dtos.ResponseDTO;
import top.rrricardo.postletterserver.dtos.UserDTO;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.User;
import top.rrricardo.postletterserver.utils.ControllerBase;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends ControllerBase {
    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
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
}
