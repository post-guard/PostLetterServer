package top.rrricardo.postletterserver.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletterserver.models.Session;

import java.util.List;

@Mapper
public interface SessionMapper {
    List<Session> getSessions();

    @Nullable
    Session getSessionById(int id);

    void createSession(@NotNull Session session);

    void updateSession(@NotNull Session session);

    void deleteSession(int id);
}
