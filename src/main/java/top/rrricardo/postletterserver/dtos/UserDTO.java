package top.rrricardo.postletterserver.dtos;

import top.rrricardo.postletterserver.models.User;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private int id;

    private String nickname;

    private String username;

    public UserDTO(User user) {
        id = user.getId();
        nickname = user.getNickname();
        username = user.getUsername();
    }

    public UserDTO(int id, String nickname, String username) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
