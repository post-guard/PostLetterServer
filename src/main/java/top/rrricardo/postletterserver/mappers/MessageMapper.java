package top.rrricardo.postletterserver.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import top.rrricardo.postletterserver.models.Message;

import java.util.List;

@Mapper
public interface MessageMapper {
    @NotNull
    List<Message> getMessages();

    @NotNull
    List<Message> getMessagesBySessionId(int sessionId);

    @NotNull
    Message getMessageById(int id);

    void createMessage(@NotNull Message message);

    void updateMessage(@NotNull Message message);

    void deleteMessage(int id);
}
