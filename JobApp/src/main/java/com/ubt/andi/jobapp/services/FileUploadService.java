package com.ubt.andi.jobapp.services;

import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Profile;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void saveImage(Profile profile, MultipartFile file);
    void deleteImage(Profile profile);
    void saveDocument(Application application, MultipartFile file);
    void deleteDocument(Application application);
}
