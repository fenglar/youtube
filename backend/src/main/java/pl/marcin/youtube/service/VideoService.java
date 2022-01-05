package pl.marcin.youtube.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    public void uploadVideo(MultipartFile multipartFile){
        // upload file to AWS s3
        // save video data to database
    }
}
