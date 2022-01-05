package pl.marcin.youtube.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.marcin.youtube.model.Video;

public interface VideoRepository extends MongoRepository<Video, String> {
}
