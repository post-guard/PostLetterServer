package top.rrricardo.postletterserver.models;

import org.jetbrains.annotations.NotNull;

public class User {
    private int id;

    @NotNull
    private String username;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    public User(@NotNull String emailAddress, @NotNull String nickname, @NotNull String password) {
        this.username = emailAddress;
        this.nickname = nickname;
        this.password = password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    @NotNull
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@NotNull String nickname) {
        this.nickname = nickname;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }
}
