package top.rrricardo.postletterserver.dtos;

/**
 * 登录数据类
 */
public class LoginDTO {
    private int userId;

    private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
