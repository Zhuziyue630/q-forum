package com.boda.qaforum.controller;

import com.boda.qaforum.model.Discussion;
import com.boda.qaforum.service.DiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DiscussionService discussionService;

    // 只保留首页映射，删除所有其他映射
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "问答论坛首页");

        // 获取最新的5个讨论
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        List<Discussion> recentDiscussions = discussionService.getAllDiscussions(pageable).getContent();
        model.addAttribute("recentDiscussions", recentDiscussions);

        return "index";
    }
}