package top.rrricardo.postletterserver.exceptions;

/**
 * 登录引发的异常
 */
public class LoginException extends Exception {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
