package com.boda.qaforum.repository;

import com.boda.qaforum.model.Discussion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    Page<Discussion> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT d FROM Discussion d LEFT JOIN d.replies r GROUP BY d ORDER BY COUNT(r) DESC, d.createdAt DESC")
    Page<Discussion> findAllByOrderByReplyCountDesc(Pageable pageable);
}