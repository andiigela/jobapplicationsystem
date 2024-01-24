package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Post;
import com.ubt.andi.jobapp.models.Share;

public interface ShareService {
    Share findShareById(Long id );
    Share findShareByAndUser(Long id );
    void createShare(Share share , Post post);
    void editShare(Share share  );
    void deleteShare(Long id );

}
