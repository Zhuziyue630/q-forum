package com.boda.qaforum.repository;

import com.boda.qaforum.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findByDiscussionIdOrderByCreatedAtAsc(Long discussionId);

    int countByDiscussionId(Long discussionId);
}