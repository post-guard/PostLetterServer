package top.rrricardo.postletterserver.dtos;

/**
 * 登录数据类
 */
public class LoginDTO {
    private String username;

    private String password;

    private String hostname;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
