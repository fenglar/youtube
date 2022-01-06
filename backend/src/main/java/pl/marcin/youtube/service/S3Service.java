package pl.marcin.youtube.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    public static final String YOUTUBE_DEMO = "youtube-mw";

    private final AmazonS3Client awsS3Client;

    @Override
    public String uploadFile(MultipartFile file) {

        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        var key = UUID.randomUUID().toString() + "." + filenameExtension;

        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {
            awsS3Client.putObject(YOUTUBE_DEMO, key, file.getInputStream(), metadata);
        } catch (IOException ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An exception occured while uploading a file");
        }
        awsS3Client.setObjectAcl(YOUTUBE_DEMO, key, CannedAccessControlList.PublicRead);
        return awsS3Client.getResourceUrl(YOUTUBE_DEMO, key);
    }
}
