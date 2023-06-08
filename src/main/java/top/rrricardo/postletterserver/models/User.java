package top.rrricardo.postletterserver.models;

public class User {
    private int id;
    private String emailAddress;
    private String nickname;

    private String password;

    public User(String emailAddress, String nickname, String password) {
        this.emailAddress = emailAddress;
        this.nickname = nickname;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
