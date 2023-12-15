package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.Job;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.services.JobService;
import com.ubt.andi.jobapp.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    private PostService postService;
    private static final int PAGE_SIZE = 5;
    public HomeController(PostService postService){
        this.postService=postService;
    }
    @GetMapping("/")
    public String getHomeView(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Post> posts = postService.getPostsByPage(pageable);
        model.addAttribute("posts",posts);
        return "home";
    }
    @GetMapping("/posts/create")
    public String getCreatePostView(Model model){
        model.addAttribute("post", new Post());
        return "create-post";
    }
    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute("post") Post post){
        postService.createPost(post);
        return "redirect:/";
    }
    @GetMapping("/profile/posts")
    public String getUserPosts(@RequestParam(value = "page",defaultValue = "0") String page,Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Post> userPosts= postService.getPostsByUserId(pageable);
        model.addAttribute("userPosts",userPosts);
        return "user-posts";
    }
    @GetMapping("/profile/posts/edit/{id}")
    public String getEditPostView(@PathVariable("id") Long id, Model model){
        Post post = postService.getPostById(id);
        model.addAttribute("postEdit",post);
        return "edit-post";
    }
    @PostMapping("/profile/posts/edit")
    public String editPost(@ModelAttribute("postEdit") Post post){
        postService.editPost(post);
        return "redirect:/profile/posts";
    }
}
