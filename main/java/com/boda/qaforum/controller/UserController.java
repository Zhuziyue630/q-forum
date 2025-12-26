package com.boda.qaforum.controller;

import com.boda.qaforum.dto.RegisterDTO;
import com.boda.qaforum.model.User;
import com.boda.qaforum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录页面 - GET /login
     */
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("title", "用户登录");
        return "user/login";
    }

    /**
     * 注册页面 - GET /register
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("title", "用户注册");
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO());
        }
        return "user/register";
    }

    /**
     * 处理注册 - POST /register
     */
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDTO") RegisterDTO registerDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // 验证表单
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDTO", bindingResult);
            return "redirect:/register";
        }

        // 检查用户名和邮箱是否已存在
        if (userService.existsByUsername(registerDTO.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "用户名已存在");
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/register";
        }

        if (userService.existsByEmail(registerDTO.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "邮箱已存在");
            redirectAttributes.addFlashAttribute("registerDTO", registerDTO);
            return "redirect:/register";
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword()); // 注意：实际应该加密

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
        return "redirect:/login";
    }
}