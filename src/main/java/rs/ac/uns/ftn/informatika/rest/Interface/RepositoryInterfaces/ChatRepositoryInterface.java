package rs.ac.uns.ftn.informatika.rest.Interface.RepositoryInterfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.informatika.rest.domain.Chat;
import rs.ac.uns.ftn.informatika.rest.domain.ChatMessage;

import java.util.List;

public interface ChatRepositoryInterface extends JpaRepository<Chat, Integer> {
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.username = :username")
    List<Chat> findChatsByParticipant(String username);


    Chat findChatById(int id);

    @Query("SELECT m FROM ChatMessage m WHERE m.chatId = :chatId ORDER BY m.creationTime DESC")
    List<ChatMessage> findLastTenMessagesByChatId(int chatId, org.springframework.data.domain.Pageable pageable);
}