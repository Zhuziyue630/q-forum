package com.boda.qaforum.service;

import com.boda.qaforum.model.Discussion;
import com.boda.qaforum.model.Reply;
import com.boda.qaforum.repository.DiscussionRepository;
import com.boda.qaforum.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DiscussionService {

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private ReplyRepository replyRepository;

    /**
     * 获取所有讨论（分页）
     */
    public Page<Discussion> getAllDiscussions(Pageable pageable) {
        return discussionRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * 按热度获取所有讨论（分页）
     */
    public Page<Discussion> getAllDiscussionsByPopularity(Pageable pageable) {
        return discussionRepository.findAllByOrderByReplyCountDesc(pageable);
    }

    /**
     * 根据ID获取讨论
     */
    public Optional<Discussion> getDiscussionById(Long id) {
        return discussionRepository.findById(id);
    }

    /**
     * 保存讨论
     */
    public Discussion saveDiscussion(Discussion discussion) {
        return discussionRepository.save(discussion);
    }

    /**
     * 保存回复
     */
    @Transactional
    public Reply saveReply(Reply reply) {
        Reply savedReply = replyRepository.save(reply);

        // 更新讨论的回复列表
        Discussion discussion = reply.getDiscussion();
        discussion.getReplies().add(savedReply);
        discussionRepository.save(discussion);

        return savedReply;
    }

    /**
     * 增加浏览次数
     */
    @Transactional
    public void incrementViewCount(Long discussionId) {
        discussionRepository.findById(discussionId).ifPresent(discussion -> {
            discussion.incrementViewCount();
            discussionRepository.save(discussion);
        });
    }

    /**
     * 根据讨论ID获取回复
     */
    public java.util.List<Reply> getRepliesByDiscussionId(Long discussionId) {
        return replyRepository.findByDiscussionIdOrderByCreatedAtAsc(discussionId);
    }

    /**
     * 获取讨论的回复数量
     */
    public int getReplyCount(Long discussionId) {
        return replyRepository.countByDiscussionId(discussionId);
    }
}