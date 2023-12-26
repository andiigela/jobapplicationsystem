package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.ProfileRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Override
    public void saveImage(Profile profile, MultipartFile file) {
        if(profile != null && !file.isEmpty()){
            try{
                byte[] imageData = file.getBytes();
                String fileName = SecurityContextHolder.getContext().getAuthentication().getName() + ".png";
                String filePath = "src/main/resources/static/images/" + fileName;

                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(imageData);
                System.out.println(Arrays.toString(profile.getImageData()));
                profile.setImagePath("/static/images/" + fileName);
                profile.setImageData(imageData);
            }catch (FileNotFoundException fe){
                System.out.println(fe.getMessage());
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }
}
