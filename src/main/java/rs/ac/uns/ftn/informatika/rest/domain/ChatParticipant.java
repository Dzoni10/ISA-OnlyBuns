package rs.ac.uns.ftn.informatika.rest.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class ChatParticipant {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "join_time", nullable = false)
    private LocalDateTime joinTime;

    public ChatParticipant() {}

    public ChatParticipant(String username, LocalDateTime joinTime) {
        this.username = username;
        this.joinTime = joinTime;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public LocalDateTime getJoinTime() { return joinTime; }
    public void setJoinTime(LocalDateTime joinTime) { this.joinTime = joinTime; }
}

