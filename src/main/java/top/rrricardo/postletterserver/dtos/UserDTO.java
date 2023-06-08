package top.rrricardo.postletterserver.dtos;

import top.rrricardo.postletterserver.models.User;

import java.io.Serializable;

public class UserDTO implements Serializable {
    private int id;

    private String nickname;

    private String emailAddress;

    public UserDTO(User user) {
        id = user.getId();
        nickname = user.getNickname();
        emailAddress = user.getEmailAddress();
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
