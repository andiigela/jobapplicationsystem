package com.ubt.andi.jobapp.controllers;
import com.ubt.andi.jobapp.exception.ShareNotFoundException;
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
    private final NotificationService notificationService;
    private final ShareService shareService;
    private static final int PAGE_SIZE = 5;
    public HomeController(PostService postService,UserService userService,LikedPostsService likedPostsService,
                          CommentService commentService,JobService jobService,NotificationService notificationService,ShareService shareService){
        this.postService=postService;
        this.userService=userService;
        this.likedPostsService=likedPostsService;
        this.commentService=commentService;
        this.jobService=jobService;
        this.notificationService=notificationService;
        this.shareService=shareService;
    }
    @GetMapping("/")
    public String getHomeView(@RequestParam(value = "page", defaultValue = "0") String page, Model model) {
        int pageNumber = Integer.parseInt(page);
        if (pageNumber > 0) {
            pageNumber -= 1;
        }
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Post> posts = postService.getPostsByPage(pageable);
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if (profile == null) return "redirect:/profile/view/" + user.getUsername();
        if (user != null) {
            model.addAttribute("user", user);
        }

        Map<Long, Boolean> userLikes = new HashMap<>();
        Map<Long, Boolean> userShares = new HashMap<>();

        for (Post post : posts) {
            if (post.getJob() != null) {
                if (!post.getJob().isActive()) {
                    post.setJob(null);
                    postService.editPost(post);
                }
            }

            // Check if the post is liked by the user
            LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
            boolean likedByUser = false;
            if (likedPosts != null) {
                likedByUser = true;
            }
            userLikes.put(post.getId(), likedByUser);

            // Check if the post is shared by the user
            Share postIsShared = shareService.isPostSharedByUser(user, post);
            boolean sharedByUser = false;
            if (postIsShared != null) {
                sharedByUser = true;
            }
            userShares.put(post.getId(), sharedByUser);
        }

        model.addAttribute("userLikes", userLikes);
        model.addAttribute("userShares", userShares);  // Add userShares to the model
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("profile", profile);


        return "index";
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
            Job job = jobService.getJobByIdAndUser(jobId);
            if(job.isActive()){
                post.setJob(job);
                job.setPost(post);
            }
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
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        Map<Long, Boolean> userLikes = new HashMap<>();
        Map<Long, Boolean> userShares = new HashMap<>();
        for (Post post : userPosts) {
            if (post.getJob() != null) {
                if (!post.getJob().isActive()) {
                    post.setJob(null);
                    postService.editPost(post);
                }
            }

            // Check if the post is liked by the user
            LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
            boolean likedByUser = false;
            if (likedPosts != null) {
                likedByUser = true;
            }
            userLikes.put(post.getId(), likedByUser);

            // Check if the post is shared by the user
            Share postIsShared = shareService.isPostSharedByUser(user, post);
            boolean sharedByUser = false;
            if (postIsShared != null) {
                sharedByUser = true;
            }
            userShares.put(post.getId(), sharedByUser);
        }
        model.addAttribute("userLikes",userLikes);
        model.addAttribute("userShares", userShares);
        model.addAttribute("userPosts",userPosts);
        model.addAttribute("profile",profile);
        return "user-posts";
    }
    @GetMapping("/profile/posts/edit/{id}")
    public String getEditPostView(@PathVariable("id") Long id, Model model){
        Post post = postService.getPostByIdAndUser(id);
        if(post == null){
            return "redirect:/";
        }
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("user",user);
        model.addAttribute("postEdit",post);
        return "edit-post";
    }
    @PostMapping("/profile/posts/edit")
    public String editPost(@ModelAttribute("postEdit") Post post,@RequestParam("jobId") Long jobId){
        if(jobId != null && jobId != 0){
            Job job = jobService.getJobByIdAndUser(jobId);
            if(job.isActive()){
                post.setJob(job);
                job.setPost(post);
            }
        }
        postService.editPost(post);
        return "redirect:/profile/posts";
    }
    @PostMapping("/profile/posts/delete/{id}")
    public String deletePost(@PathVariable("id") Long id){
        Post post = postService.getPostByIdAndUser(id);
        if(post == null){
            return "redirect:/";
        }
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
            notificationService.sendPostNotification(post.getAppUser().getProfile(),post,"Like"); // send Like to Post's profile
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
    @PostMapping("/share/{postId}")
    public String sharePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()); //Dreni
        Post originalPost = postService.getPostById(postId); // <- id (postId)
        Share existingSharedPost = shareService.isPostSharedByUser(user, originalPost);
        Long numberOfShares = originalPost.getNumberOfShares();

        if (existingSharedPost == null) {
            Post sharedPost = new Post();//223
            sharedPost.setDescription(originalPost.getDescription());
            sharedPost.setShared(true);
            sharedPost.setJob(originalPost.getJob());
            sharedPost.setAppUser(user);
            postService.createPost(sharedPost); //223

            Share share = new Share(); //dreni  223
            share.setAppUser(user);
            share.setPost(originalPost);
            share.setSharedPost(sharedPost);
            shareService.createShare(share, originalPost);

            originalPost.setNumberOfShares(numberOfShares + 1);
            postService.editPost(originalPost);
            notificationService.sendPostNotification(originalPost.getAppUser().getProfile(), originalPost, "Share");
        } else {



            if (existingSharedPost.getPost() != null) {
                if (numberOfShares != null && numberOfShares > 0) {
                    originalPost.setNumberOfShares(numberOfShares - 1);
                    postService.editPost(originalPost);
                    postService.deletePost(existingSharedPost.getSharedPost().getId());
                }
            }

        }
        return "redirect:/";
    }



    @GetMapping("/posts/{id}/comment")
    public String getCommentPostView(@PathVariable("id") Long postId,Model model){
        Post post = postService.getPostById(postId);
        Map<Long, Boolean> userLikes = new HashMap<>();
        boolean likedByUser = false;
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
        if(likedPosts != null){
            likedByUser = true;
        }
        userLikes.put(post.getId(), likedByUser);
        model.addAttribute("userLikes",userLikes);
        model.addAttribute("post",post);
        model.addAttribute("profile",profile);
        return "comment-section";
    }
    @PostMapping("/posts/{id}/comment")
    public String commentPost(@PathVariable("id") Long postId,@RequestParam("description") String description){
        Post post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setDescription(description);
        commentService.createComment(comment,post);
        post.setNumberOfComments(post.getNumberOfComments()+1); // increase number of comments
        notificationService.sendPostNotification(post.getAppUser().getProfile(),post,"Comment"); // send Like to Post's profile
        postService.editPost(post);
        return "redirect:/posts/"+postId+"/comment";
    }
    @GetMapping("/posts/{postId}/comment/{commentId}")
    public String editCommentView(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId,Model model){
        Post post = postService.getPostById(postId);
        Comment comment = commentService.findCommentByIdAndUser(commentId);
        if(comment == null) return "redirect:/posts/"+postId+"/comment";
        Map<Long, Boolean> userLikes = new HashMap<>();
        boolean likedByUser = false;
        AppUser user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Profile profile = user.getProfile();
        if(profile == null) return "redirect:/profile/view/"+user.getUsername();
        LikedPosts likedPosts = likedPostsService.isPostLikedByUser(user, post);
        if(likedPosts != null){
            likedByUser = true;
        }
        userLikes.put(post.getId(), likedByUser);
        model.addAttribute("userLikes",userLikes);
        model.addAttribute("post",post);
        model.addAttribute("comment",comment);
        model.addAttribute("commentId",commentId);
        model.addAttribute("profile",profile);
        return "editcomment-section";
    }
    @PostMapping("/posts/{postId}/comment/{commentId}")
    public String editComment(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId,
                              @ModelAttribute("comment") Comment comment){
        Comment commentDb = commentService.findCommentByIdAndUser(commentId);
        if(commentDb == null) return "redirect:/posts/"+postId+"/comment";
        commentService.editComment(comment);
        return "redirect:/posts/"+postId+"/comment";
    }
    @PostMapping("/posts/delete/{postId}/{commentId}")
    public String deleteComment(@PathVariable("postId") Long postId,@PathVariable("commentId") Long commentId){
        Comment commentDb = commentService.findCommentByIdAndUser(commentId);
        if(commentDb == null) return "redirect:/posts/"+postId+"/comment";
        Post post = postService.getPostById(postId);
        commentService.deleteComment(commentId);
        if(post.getNumberOfComments() != 0){
            post.setNumberOfComments(post.getNumberOfComments()-1);
            postService.editPost(post);
        }
        return "redirect:/posts/"+postId+"/comment";
    }
}