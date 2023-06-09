package top.rrricardo.postletterserver.services;

import top.rrricardo.postletterserver.dtos.LoginDTO;
import top.rrricardo.postletterserver.exceptions.LoginException;
import top.rrricardo.postletterserver.models.User;

public interface UserService {
    void register(User user);

    void login(LoginDTO loginDTO) throws LoginException;
}
