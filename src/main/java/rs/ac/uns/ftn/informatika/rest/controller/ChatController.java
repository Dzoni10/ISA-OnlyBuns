package rs.ac.uns.ftn.informatika.rest.controller;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.rest.domain.ChatMessage;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.ChatParticipant;
import rs.ac.uns.ftn.informatika.rest.dto.ChatMessageDTO;
import rs.ac.uns.ftn.informatika.rest.dto.ChatDTO;
import rs.ac.uns.ftn.informatika.rest.dto.ChatParticipantDTO;
import rs.ac.uns.ftn.informatika.rest.paged_result.PagedResults;
import rs.ac.uns.ftn.informatika.rest.service.ChatService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
@Transactional
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Operation(description = "Create new chat", method = "POST")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chatDTO) {
        Chat newChat = new Chat(chatDTO.name, chatDTO.adminUsername);

        // Dodaj učesnike u chat. Koristi ChatParticipant objekat umesto samo stringa (korisničkog imena)
        for (ChatParticipantDTO participantDTO : chatDTO.participants) {
            // Kreiraj ChatParticipant objekat
            ChatParticipant participant = new ChatParticipant(participantDTO.username, LocalDateTime.now());
            newChat.addParticipant(participant.getUsername());
        }

        Chat savedChat = chatService.save(newChat);
        return new ResponseEntity<>(new ChatDTO(savedChat), HttpStatus.CREATED);
    }


    @GetMapping("/user/{username}")
    public ResponseEntity<List<ChatDTO>> getChatsByUser(@PathVariable String username) {
        List<Chat> chats = chatService.findChatsByUser(username);
        List<ChatDTO> chatDTOs = chats.stream()
                .map(ChatDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chatDTOs);
    }

    @GetMapping("/check-user/{username}")
    public ResponseEntity<Void> checkUserExist(@PathVariable String username) {
            chatService.checkUserExist(username);
            return ResponseEntity.ok().build();
    }


    @PostMapping("/{chatId}/add-user")
    public ResponseEntity<Void> addUserToChat(
            @PathVariable int chatId,
            @RequestBody Map<String, String> payload
    ) {
        chatService.addUserToChat(chatId, payload.get("username"));
        return ResponseEntity.ok().build();
    }
    @Operation(description = "Delete user From Chat", method = "DELETE")
    @DeleteMapping("/{chatId}/remove-user")
    public ResponseEntity<Void> removeUserFromChat(
            @PathVariable int chatId,
            @RequestBody Map<String, String> payload
    ) {
        chatService.removeUserFromChat(chatId, payload.get("username"));
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(@DestinationVariable int chatId, ChatMessageDTO message) {
        System.out.println("Primljena poruka u chatu " + chatId + " od korisnika " + message.username + ": " + message.text + "ID PORUKE JE " + message.id);
        ChatMessage savedMessage = chatService.saveMessage(new ChatMessage(chatId, message.username, message.text));
        messagingTemplate.convertAndSend("/topic/chat/" + chatId, new ChatMessageDTO(savedMessage));
    }


    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable int chatId) {
        Chat chat = chatService.findChatById(chatId);
        if (chat == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(new ChatDTO(chat));
    }
    @GetMapping("/{chatId}/access")
    public ResponseEntity<Boolean> checkAccessToChat(@PathVariable int chatId, @RequestParam String username) {
        boolean canAccess = chatService.canAccessChat(chatId, username);
        return ResponseEntity.ok(canAccess);
    }

    @GetMapping("/{chatId}/last-messages")
    public ResponseEntity<List<ChatMessageDTO>> getLastMessages(@PathVariable int chatId) {
        List<ChatMessage> messages = chatService.getLastTenMessages(chatId);
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }
}