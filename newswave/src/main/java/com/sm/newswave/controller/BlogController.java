package com.sm.newswave.controller;

import com.sm.newswave.model.BlogComment;
import com.sm.newswave.model.BlogPost;
import com.sm.newswave.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping
    public String getBlog(@RequestParam(defaultValue = "0") int page, HttpServletRequest request, Model model) {
        model.addAttribute("postPage", blogService.blogPostPageRequest(page, 12));
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("title", "Blog");
        model.addAttribute("content", "blog/index");
        return "layout";
    }

    @GetMapping("/{path}")
    public String getPostByPath(@PathVariable("path") String path, HttpServletRequest request, Model model) {
        BlogPost blogPost = blogService.findBlogPostByBlogUrl(path);
        model.addAttribute("post", blogPost);
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("title", blogPost.getTitle());
        model.addAttribute("content", "blog/post");
        return "layout";
    }

    @PostMapping("/{postId}/comment")
    public String addComment(@PathVariable Long postId,
                             @RequestParam(required = false) Long comId,
                             BlogComment comment) {
        blogService.addComment(postId, comId, comment);
        return "redirect:/blog/" + blogService.findBlogPostById(postId).getBlogUrl();
    }

    @PostMapping("/{postId}/react/{reaction}")
    @ResponseBody
    public String addReaction(@PathVariable Long postId, @PathVariable String reaction) {
        int newCount = blogService.addReaction(postId, reaction);
        return "{\"success\": true, \"newCount\": " + newCount + "}";
    }

}
