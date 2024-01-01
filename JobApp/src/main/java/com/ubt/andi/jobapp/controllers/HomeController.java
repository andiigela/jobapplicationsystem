package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.models.*;
import com.ubt.andi.jobapp.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.security.SecurityConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final JobService jobService;
    private final LikedPostsService likedPostsService;
    private static final int PAGE_SIZE = 5;
    public HomeController(PostService postService,UserService userService,LikedPostsService likedPostsService,
                          CommentService commentService,JobService jobService){
        this.postService=postService;
        this.userService=userService;
        this.likedPostsService=likedPostsService;
        this.commentService=commentService;
        this.jobService=jobService;
    }
    @GetMapping("/")
    public String getHomeView(@RequestParam(value = "page",defaultValue = "0") String page, Model model){
        int pageNumber = Integer.parseInt(page);
        if(pageNumber > 0){
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber,PAGE_SIZE);
        Page<Post> posts = postService.getPostsByPage(pageable);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(user != null){
            model.addAttribute("user",user);
        }
        Map<Long, Boolean> userLikes = new HashMap<>();
        for (Post post : posts) {
            LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
            boolean likedByUser = false;
            if(likedPosts != null){
                likedByUser = true;
            }
            userLikes.put(post.getId(), likedByUser);
        }


        model.addAttribute("userLikes",userLikes);
        model.addAttribute("posts",posts);
        model.addAttribute("currentPage",page);
        return "home";
    }
    @GetMapping("/posts/create")
    public String getCreatePostView(Model model){
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("post", new Post());
        model.addAttribute("user", user);
        return "create-post";
    }
    @PostMapping("/posts/create")
    public String createPost(@ModelAttribute("post") Post post, @RequestParam("jobId") Long jobId){
        if(jobId != null && jobId != 0){
            Job job = jobService.getJob(jobId);
            post.setJob(job);
            job.setPost(post);
        }
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
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Map<Long, Boolean> userLikes = new HashMap<>();
        for (Post post : userPosts) {
            LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
            boolean likedByUser = false;
            if(likedPosts != null){
                likedByUser = true;
            }
            userLikes.put(post.getId(), likedByUser);
        }
        model.addAttribute("userLikes",userLikes);
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
    @PostMapping("/profile/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
        return "redirect:/";
    }
    @PostMapping("/like/{postId}")
    public String doLike(@PathVariable("postId") Long postId, HttpServletRequest request){
        Post post = postService.getPostById(postId);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        LikedPosts existingLikedPosts = likedPostsService.isPostLikedByUser(user,post);
        Long numberOfLikes = post.getNumberOfLikes();
        if(existingLikedPosts == null){
            LikedPosts likedPosts = new LikedPosts();
            likedPosts.setAppUser(user);
            likedPosts.setPost(post);
            likedPostsService.createLikedPost(likedPosts);
            post.setNumberOfLikes(numberOfLikes+1);
        } else {
            likedPostsService.deleteLikedPost(existingLikedPosts);
            if(numberOfLikes != 0){
                post.setNumberOfLikes(numberOfLikes-1);
            }
        }
        postService.editPost(post);
        String referrer = request.getHeader("referer");

        if(referrer != null && referrer.contains("/posts/" + postId + "/comment")){
            return "redirect:/posts/" + postId + "/comment";
        }
        if(referrer != null && referrer.contains("/profile/posts")){
            return "redirect:/profile/posts";
        }
        return "redirect:/";
    }
    @GetMapping("/posts/{id}/comment")
    public String getCommentPostView(@PathVariable("id") Long postId,Model model){
        Post post = postService.getPostById(postId);
        Map<Long, Boolean> userLikes = new HashMap<>();
        boolean likedByUser = false;
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
        if(likedPosts != null){
            likedByUser = true;
        }
        userLikes.put(post.getId(), likedByUser);
        model.addAttribute("userLikes",userLikes);
        model.addAttribute("post",post);
        return "comment-section";
    }
    @PostMapping("/posts/{id}/comment")
    public String commentPost(@PathVariable("id") Long postId,@RequestParam("description") String description){
        Post post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setDescription(description);
        commentService.createComment(comment,post);
        post.setNumberOfComments(post.getNumberOfComments()+1); // increase number of comments
        postService.editPost(post);
        return "redirect:/posts/"+postId+"/comment";
    }
    @GetMapping("/posts/{postId}/comment/{commentId}")
    public String editCommentView(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId,Model model){
        Post post = postService.getPostById(postId);
        Comment comment = commentService.findCommentById(commentId);
        Map<Long, Boolean> userLikes = new HashMap<>();
        boolean likedByUser = false;
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
        if(likedPosts != null){
            likedByUser = true;
        }
        userLikes.put(post.getId(), likedByUser);
        model.addAttribute("userLikes",userLikes);
        model.addAttribute("post",post);
        model.addAttribute("comment",comment);
        model.addAttribute("commentId",commentId);
        return "editcomment-section";
    }
    @PostMapping("/posts/{postId}/comment/{commentId}")
    public String editComment(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId,
                              @ModelAttribute("comment") Comment comment){
        commentService.editComment(comment);
//        if(post.getNumberOfComments() != 0){
//            post.setNumberOfLikes(post.getNumberOfComments()-1);
//            postService.editPost(post);
//        }
        return "redirect:/posts/"+postId+"/comment";
    }
    @PostMapping("/posts/delete/{postId}/{commentId}")
    public String deleteComment(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId){
        Post post = postService.getPostById(postId);
        commentService.deleteComment(commentId);
        if(post.getNumberOfComments() != 0){
            post.setNumberOfComments(post.getNumberOfComments()-1);
            postService.editPost(post);
        }
        return "redirect:/posts/"+postId+"/comment";
    }
}
