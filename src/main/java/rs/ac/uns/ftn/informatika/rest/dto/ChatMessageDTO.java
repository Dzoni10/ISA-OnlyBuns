package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.ChatMessage;

import java.time.LocalDateTime;

public class ChatMessageDTO {
    public int id;
    public int chatId;
    public String username;
    public String text;
    public LocalDateTime creationTime;

    public ChatMessageDTO() {}

    public ChatMessageDTO(ChatMessage message) {
        this.id = message.getId();
        this.chatId = message.getChatId();
        this.username = message.getUsername();
        this.text = message.getText();
        this.creationTime = message.getCreationTime();
    }
}
