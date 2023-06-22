package top.rrricardo.postletterserver.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Group implements Serializable {
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String details;

    private int sessionId;

    public Group(@NotNull String name, @NotNull String details, int sessionId) {
        this.name = name;
        this.details = details;
        this.sessionId = sessionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getDetails() {
        return details;
    }

    public void setDetails(@NotNull String details) {
        this.details = details;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
