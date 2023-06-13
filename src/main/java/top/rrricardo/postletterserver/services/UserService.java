package top.rrricardo.postletterserver.services;

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

    public void register(User user) {
        user.setPassword(sha256Password(user.getPassword()));

        userMapper.createUser(user);
    }

    public void login(LoginDTO loginDTO) throws LoginException {
        var user =  userMapper.getUserById(loginDTO.getUserId());

        if (user == null) {
            throw new LoginException("邮简ID不存在");
        }

        if (!user.getPassword().equals(sha256Password(loginDTO.getPassword()))) {
            throw new LoginException("密码错误");
        }
    }

    private String sha256Password(String password) {
        for (var i = 0; i < 1000; i++) {
            password = EncryptSha256Util.sha256String(password + passwordSalt);
        }

        return password;
    }
}
