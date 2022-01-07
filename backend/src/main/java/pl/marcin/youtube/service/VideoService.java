package pl.marcin.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.marcin.youtube.dto.UploadVideoResponse;
import pl.marcin.youtube.dto.VideoDto;
import pl.marcin.youtube.model.Video;
import pl.marcin.youtube.repository.VideoRepository;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);
        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse((savedVideo.getId()), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        Video savedVideo = videoRepository.findById(videoDto.getId()).orElseThrow(() ->
                new IllegalArgumentException("Cannot find video by id - " + videoDto.getId()));
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        var savedVideo = getVideoById(videoId);
        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    Video getVideoById(String videoId){
         return videoRepository.findById(videoId).orElseThrow(() ->
                new IllegalArgumentException("Cannot find video by id - " + videoId));

    }
}
