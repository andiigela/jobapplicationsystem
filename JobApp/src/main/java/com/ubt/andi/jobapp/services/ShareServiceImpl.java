package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;
import com.ubt.andi.jobapp.repositories.ShareRepository;
import com.ubt.andi.jobapp.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class ShareServiceImpl implements ShareService {

    ShareRepository shareRepository;

    UserRepository userRepository;

    public ShareServiceImpl(ShareRepository shareRepository,UserRepository userRepository) {
        this.shareRepository = shareRepository;
        this.userRepository=userRepository;
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
    public void createShare(Share share, Post post) {
        AppUser user = userRepository.findAppUserByUsername((SecurityContextHolder.getContext().getAuthentication().getName()));
        if (user != null || share !=null || post != null){
            share.setPost(post);
            share.setAppUser(user);
            shareRepository.save(share);
        }
    }

    @Override
    public void editShare(Share share) {
        if (share == null) return;
        Share sharedb = shareRepository.findById(share.getId()).get();
        sharedb.setDescription(share.getDescription());
        shareRepository.save(sharedb);
    }

    @Override
    public void deleteShare(Long id) {
        if (id ==null) return;
        shareRepository.deleteById(id);
    }
}
