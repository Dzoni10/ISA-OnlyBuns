package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces.ChatRepositoryInterface;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.ChatMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional

public class ChatService {
    @Autowired
    private ChatRepositoryInterface chatRepositoryInterface;
    @Autowired
    private UserService userService;
    public Chat save(Chat chat) {
        return chatRepositoryInterface.save(chat);
    }

    public Chat findChatById(int id) {
        return chatRepositoryInterface.findChatById(id);
    }

    @Transactional(readOnly = true)
    public List<Chat> findChatsByUser(String username) {
        List<Chat> chats = chatRepositoryInterface.findChatsByParticipant(username);
        // Ovo će "dotaći" kolekcije pre nego što se zatvori sesija
        chats.forEach(chat -> {
            chat.getParticipants().size();
            chat.getMessages().size();
        });
        return chats;
    }
    public void checkUserExist(String username) {
        if (!userService.getAllLoggedInUsernames().contains(username))
            throw new IllegalArgumentException("User with username " + username + " does not exist.");
    }

        public void addUserToChat(int chatId, String username) {
        if (!userService.getAllUsernames().contains(username))
            throw new IllegalArgumentException("User with username " + username + " does not exist.");
        Chat chat = findChatById(chatId);
        if (chat != null) {
            if (chat.getParticipants().stream().anyMatch(p -> p.getUsername().equals(username)))
                throw new IllegalArgumentException("The user is already a member of this chat.");
            chat.addParticipant(username);
            chatRepositoryInterface.save(chat);
        }
    }

    public void removeUserFromChat(int chatId, String username) {
        Chat chat = findChatById(chatId);
        if (chat != null) {
            chat.getParticipants().removeIf(p -> p.getUsername().equals(username));
            chatRepositoryInterface.save(chat);
        }
    }

    public List<ChatMessage> getLastTenMessages(int chatId) {
        return chatRepositoryInterface.findLastTenMessagesByChatId(
                chatId, PageRequest.of(0, 10)
        );
    }

    public ChatMessage saveMessage(ChatMessage message) {
        Chat chat = findChatById(message.getChatId());
        chat.addMessage(message);
        chatRepositoryInterface.save(chat);
        return message;
    }

    public boolean canAccessChat(int chatId, String username) {
        Chat chat = findChatById(chatId);
        if (chat == null) {
            throw new IllegalArgumentException("Chat not found.");
        }
        return chat.getParticipants().stream().anyMatch(p -> p.getUsername().equals(username));
    }
}