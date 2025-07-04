package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.ChatParticipant;

import java.time.LocalDateTime;

public class ChatParticipantDTO {
    public String username;
    public LocalDateTime joinTime;

    public ChatParticipantDTO() {}

    public ChatParticipantDTO(ChatParticipant participant) {
        this.username = participant.getUsername();
        this.joinTime = participant.getJoinTime();
    }
}
