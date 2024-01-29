package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.exception.ShareNotFoundException;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;
import com.ubt.andi.jobapp.repositories.PostRepository;
import com.ubt.andi.jobapp.repositories.ShareRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShareServiceImpl implements ShareService {

    ShareRepository shareRepository;

    UserRepository userRepository;

    PostRepository postRepository;

    public ShareServiceImpl(ShareRepository shareRepository,UserRepository userRepository,PostRepository postRepository) {
        this.shareRepository = shareRepository;
        this.userRepository=userRepository;
        this.postRepository=postRepository;
    }

    @Override
    public Share findShareById(Long id) {
        if (id !=null){
            return shareRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Share findShareByAndUser(Long id) {
        AppUser user = userRepository.findAppUserByUsername((SecurityContextHolder.getContext().getAuthentication().getName()));
        if (user == null || id == null){
            return null;
        }
        return shareRepository.findShareByIdAndAppUser(id , user);
    }

    @Override
    public Share isPostSharedByUser(AppUser user, Post post) {
        if (user != null && post !=null){
            Share share = shareRepository.findByAppUserAndPost(user,post);
            if (share !=null) return share;
        }
        return null;
    }

    @Override
    public void createShare(Share share, Post post) {
        AppUser user = userRepository.findAppUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user != null && share != null && post != null) {
            try {
                share.setPost(post);
                share.setAppUser(user);
                shareRepository.save(share);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle the case where user, share, or post is null
            throw new IllegalArgumentException("User, Share, or Post cannot be null");
        }
    }

    @Override
    public void editShare(Share share) throws ShareNotFoundException {
        if (share != null) {
            Optional<Share> optionalSharedb = shareRepository.findById(share.getId());
            if (optionalSharedb.isPresent()) {
                Share sharedb = optionalSharedb.get();


                if (share.getDescription() != null) {
                    sharedb.setDescription(share.getDescription());
                }


                shareRepository.save(sharedb);
            } else {

                throw new ShareNotFoundException("Share with ID " + share.getId() + " not found.");
            }
        }
    }

    @Override
    public void deleteShare(Share share) throws ShareNotFoundException {
        if (share != null) {
            Optional<Share> optionalShare = shareRepository.findById(share.getId());
            if (optionalShare.isPresent()) {
                Share existingShare = optionalShare.get();
                Post existingPost = existingShare.getPost(); // Use the existing share to get the associated post
                Long numberOfShares = existingPost.getNumberOfShares();
                if (numberOfShares > 0) {
                    existingPost.setNumberOfShares(numberOfShares - 1);
                    postRepository.save(existingPost);
                }
                shareRepository.delete(existingShare);
                if (existingPost.getNumberOfShares() == 0) {
                    postRepository.delete(existingPost);
                }
            } else {
                throw new ShareNotFoundException("Share with ID " + share.getId() + " not found.");
            }
        }
    }



}
