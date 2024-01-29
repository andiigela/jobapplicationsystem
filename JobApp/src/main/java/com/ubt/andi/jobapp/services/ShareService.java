package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.exception.ShareNotFoundException;
import com.ubt.andi.jobapp.models.AppUser;
import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;

public interface ShareService {
    Share findShareById(Long id );
    Share findShareByAndUser(Long id );

    Share isPostSharedByUser(AppUser user , Post post);
    void createShare(Share share , Post post);
    void editShare(Share share  ) throws ShareNotFoundException;
    void deleteShare(Share share ) throws ShareNotFoundException;


}
