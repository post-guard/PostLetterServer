package top.rrricardo.postletterserver.mappers;

import org.jetbrains.annotations.NotNull;
import top.rrricardo.postletterserver.models.Participant;
import top.rrricardo.postletterserver.models.Session;

import java.util.List;

public interface ParticipantMapper {
    @NotNull
    List<Participant> getParticipants();

    @NotNull
    List<Participant> getParticipantsByUserId(int userId);

    @NotNull
    List<Participant> getParticipantsBySessionId(int sessionId);

    @NotNull
    Participant getParticipantById(int id);

    void createParticipant(@NotNull Participant participant);

    void updateParticipant(@NotNull Participant participant);

    void deleteParticipant(int id);
}
