package com.vlc3k.piasocialnetwork.repositories;

import com.vlc3k.piasocialnetwork.entities.ChatMessage;
import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select c from ChatMessage c where (c.userFrom = ?1 AND c.userTo = ?2) OR (c.userTo = ?1 AND c.userFrom = ?2) order by c.timestamp desc")
    List<ChatMessage> findChatMessagesBetweenUsers(User user1, User user2, Pageable pageable);
}
