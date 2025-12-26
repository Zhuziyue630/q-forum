package com.boda.qaforum.controller;

import com.boda.qaforum.model.Discussion;
import com.boda.qaforum.model.Reply;
import com.boda.qaforum.model.User;
import com.boda.qaforum.service.DiscussionService;
import com.boda.qaforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private UserService userService;

    /**
     * 讨论列表
     */
    @GetMapping("")
    public String listDiscussions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "latest") String sort,
            Model model) {

        Pageable pageable;
        Page<Discussion> discussions;

        if ("popular".equals(sort)) {
            // 按回复数排序
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            discussions = discussionService.getAllDiscussionsByPopularity(pageable);
        } else {
            // 默认按最新排序
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            discussions = discussionService.getAllDiscussions(pageable);
        }

        model.addAttribute("discussions", discussions);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", discussions.getTotalPages());
        model.addAttribute("sort", sort);

        return "discussions/list";
    }

    /**
     * 查看讨论详情
     */
    @GetMapping("/{id}")
    public String viewDiscussion(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Discussion> discussionOpt = discussionService.getDiscussionById(id);

        if (!discussionOpt.isPresent()) {
            return "redirect:/discussions";
        }

        Discussion discussion = discussionOpt.get();

        // 增加浏览次数
        discussionService.incrementViewCount(id);

        model.addAttribute("discussion", discussion);
        model.addAttribute("replies", discussion.getReplies());

        // 检查用户是否登录
        User currentUser = (User) session.getAttribute("currentUser");
        model.addAttribute("isLoggedIn", currentUser != null);

        return "discussions/view";
    }

    /**
     * 新建讨论页面
     */
    @GetMapping("/new")
    public String newDiscussionForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/user/login";
        }

        model.addAttribute("discussion", new Discussion());
        return "discussions/new";
    }

    /**
     * 创建新讨论
     */
    @PostMapping("/new")
    public String createDiscussion(
            @RequestParam String title,
            @RequestParam String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/user/login";
        }

        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "标题不能为空");
            return "redirect:/discussions/new";
        }

        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "内容不能为空");
            return "redirect:/discussions/new";
        }

        Discussion discussion = new Discussion();
        discussion.setTitle(title.trim());
        discussion.setContent(content.trim());
        discussion.setAuthor(currentUser);

        discussionService.saveDiscussion(discussion);

        redirectAttributes.addFlashAttribute("success", "讨论创建成功");
        return "redirect:/discussions/" + discussion.getId();
    }

    /**
     * 添加回复
     */
    @PostMapping("/{id}/replies")
    public String addReply(
            @PathVariable Long id,
            @RequestParam String content,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("error", "请先登录");
            return "redirect:/user/login";
        }

        Optional<Discussion> discussionOpt = discussionService.getDiscussionById(id);
        if (!discussionOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "讨论不存在");
            return "redirect:/discussions";
        }

        if (content == null || content.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "回复内容不能为空");
            return "redirect:/discussions/" + id;
        }

        Reply reply = new Reply();
        reply.setContent(content.trim());
        reply.setDiscussion(discussionOpt.get());
        reply.setAuthor(currentUser);

        discussionService.saveReply(reply);

        redirectAttributes.addFlashAttribute("success", "回复成功");
        return "redirect:/discussions/" + id;
    }
}