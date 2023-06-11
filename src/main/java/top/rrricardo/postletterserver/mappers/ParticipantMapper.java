package top.rrricardo.postletterserver.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.rrricardo.postletterserver.models.Participant;

import java.util.List;

@Mapper
public interface ParticipantMapper {
    @NotNull
    List<Participant> getParticipants();

    @NotNull
    List<Participant> getParticipantsByUserId(int userId);

    @NotNull
    List<Participant> getParticipantsBySessionId(int sessionId);

    @Nullable
    Participant getParticipantById(int id);

    void createParticipant(@NotNull Participant participant);

    void updateParticipant(@NotNull Participant participant);

    void deleteParticipant(int id);
}
