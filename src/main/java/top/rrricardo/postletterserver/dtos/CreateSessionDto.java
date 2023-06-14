package top.rrricardo.postletterserver.dtos;

import top.rrricardo.postletterserver.models.Participant;

import java.util.List;

public class CreateSessionDto {
    private String name;
    private String details;
    private List<Participant> participants;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
