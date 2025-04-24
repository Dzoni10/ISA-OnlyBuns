package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.ChatDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "admin_username", nullable = false)
    private String adminUsername;

    @ElementCollection
    @CollectionTable(name = "chat_participants", joinColumns = @JoinColumn(name = "chat_id"))
    private List<ChatParticipant> participants = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_id")
    @OrderBy("creationTime ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    public Chat() {super();}

    public Chat(String name, String adminUsername) {
        this.name = name;
        this.adminUsername = adminUsername;
        this.participants.add(new ChatParticipant(adminUsername, LocalDateTime.now()));
    }


    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
    public List<ChatParticipant> getParticipants() {  return participants; }

    public void setParticipants(List<ChatParticipant> participants) {
        this.participants = participants;
    }
    public List<ChatMessage> getMessages() { return messages; }

    public void addParticipant(String username) {
        boolean alreadyExists = participants.stream().anyMatch(p -> p.getUsername().equals(username));
        if (!alreadyExists) {
            participants.add(new ChatParticipant(username, LocalDateTime.now()));
        }
    }
    public void removeParticipant(String username) {
        participants.removeIf(p -> p.getUsername().equals(username));
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }
}

