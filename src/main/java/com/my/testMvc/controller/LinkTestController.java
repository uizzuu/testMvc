package com.my.testMvc.controller;

import com.my.testMvc.dto.Article;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/articles")
public class LinkTestController {
    @GetMapping("/main")
    public String main() {
        return "/articles/main";
    }

    @GetMapping("/list")
    public String listAll(Model model) {
        List<Map<String, String>> articles = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            Map<String, String> article = new HashMap<>();
            article.put("id", String.valueOf(i));
            article.put("title", i + "번 게시글");
            articles.add(article);
        }
        model.addAttribute("articles", articles);
        return "/articles/list_all";
    }

    @GetMapping("/{id}")
    public String listOne(@PathVariable("id")int id, Model model) {
        model.addAttribute("id", id);
        return "/articles/list_one";
    }
}
