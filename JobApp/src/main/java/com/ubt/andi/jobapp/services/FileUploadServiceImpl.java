package com.ubt.andi.jobapp.services;
import com.ubt.andi.jobapp.models.Application;
import com.ubt.andi.jobapp.models.Profile;
import com.ubt.andi.jobapp.repositories.ProfileRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final ResourceLoader resourceLoader;
    public FileUploadServiceImpl(ResourceLoader resourceLoader){
        this.resourceLoader=resourceLoader;
    }
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
                fos.close();
            }catch (FileNotFoundException fe){
                System.out.println(fe.getMessage());
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }
    @Override
    public void deleteImage(Profile profile) {
        if(profile != null){
            String fileName = SecurityContextHolder.getContext().getAuthentication().getName() + ".png";
            String filePath = "src/main/resources/static/images/" + fileName;
            try {
                Path path = Paths.get(filePath);
                File imageFile = new File(filePath);
                if(imageFile.exists()){
                    Files.delete(path);
                }
                profile.setImagePath(null);
                profile.setImageData(null);
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }

    @Override
    public void saveDocument(Application application, MultipartFile file) {
        if(!file.isEmpty()){
            try{
                byte[] fileData = file.getBytes();
                String fileName = SecurityContextHolder.getContext().getAuthentication().getName() + "_" + file.getOriginalFilename();
                String filePath = "src/main/resources/static/documents/" + fileName;
                FileOutputStream fos = new FileOutputStream(filePath);
                fos.write(fileData);
                fos.close();
                application.setFileData(fileData);
                application.setFilePath("static/documents/" + fileName);
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }
    @Override
    public void deleteDocument(Application application) {
        if(application != null){
            String fileName = application.getFilePath();
            String filePath = "src/main/resources/" + fileName; // full path
            try {
                Path path = Paths.get(filePath);
                File imageFile = new File(filePath);
                if(imageFile.exists()){
                    Files.delete(path);
                }
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    }
}
