package top.rrricardo.postletterserver.mappers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletterserver.models.Session;

import java.util.List;

public interface SessionMapper {
    List<Session> getSessions();

    @Nullable
    Session getSessionById(int id);

    void createSession(@NotNull Session session);

    void updateSession(@NotNull Session session);

    void deleteSession(int id);
}
