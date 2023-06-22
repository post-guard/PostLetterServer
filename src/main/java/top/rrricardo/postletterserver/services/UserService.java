package top.rrricardo.postletterserver.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.rrricardo.postletterserver.dtos.LoginDTO;
import top.rrricardo.postletterserver.exceptions.LoginException;
import top.rrricardo.postletterserver.mappers.UserMapper;
import top.rrricardo.postletterserver.models.User;
import top.rrricardo.postletterserver.utils.EncryptSha256Util;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Value("${user.password-salt}")
    private String passwordSalt;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void register(User user) throws IllegalArgumentException {
        var oldUser = findUserByUsername(user.getUsername());
        if (oldUser != null) {
            throw new IllegalArgumentException("用户名已经被使用");
        }

        user.setPassword(sha256Password(user.getPassword()));
        userMapper.createUser(user);
    }

    @NotNull
    public User login(LoginDTO loginDTO) throws LoginException {
        var user =  findUserByUsername(loginDTO.getUsername());

        if (user == null) {
            throw new LoginException("邮简ID不存在");
        }

        if (!user.getPassword().equals(sha256Password(loginDTO.getPassword()))) {
            throw new LoginException("密码错误");
        }

        return user;
    }

    @Nullable
    private User findUserByUsername(@NotNull String username) {
        var users = userMapper.getUsers();

        for (var user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    private String sha256Password(String password) {
        for (var i = 0; i < 1000; i++) {
            password = EncryptSha256Util.sha256String(password + passwordSalt);
        }

        return password;
    }
}
