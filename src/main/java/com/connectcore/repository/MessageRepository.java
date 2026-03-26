package com.connectcore.repository;

import com.connectcore.model.entity.Message;
import com.connectcore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiverOrderByCreatedAtAsc(User sender, User receiver);
    List<Message> findByReceiverAndIsReadFalse(User receiver);

    @Query("""
    SELECT m FROM Message m
    WHERE (
        (m.sender.id = :u1 AND m.receiver.id = :u2 AND m.deletedBySender = false)
     OR (m.sender.id = :u2 AND m.receiver.id = :u1 AND m.deletedByReceiver = false)
    )
    ORDER BY m.createdAt ASC
    """)
    List<Message> findConversation(Long u1, Long u2);
}