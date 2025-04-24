package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import java.util.List;
import java.util.stream.Collectors;

public class ChatDTO {
    public int id;
    public String name;
    public String adminUsername;
    public List<ChatParticipantDTO> participants;
    public List<ChatMessageDTO> messages;

    public ChatDTO() {}

    public ChatDTO(Chat chat) {
        this.id = chat.getId();
        this.name = chat.getName();
        this.adminUsername = chat.getAdminUsername();
        this.participants = chat.getParticipants().stream()
                .map(ChatParticipantDTO::new)
                .collect(Collectors.toList());
        this.messages = chat.getMessages().stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
    }
}
