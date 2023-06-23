package top.rrricardo.postletterserver.models;

import java.io.Serializable;

public class HeartBeat implements Serializable {
    private String hostname;

    private String token;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
