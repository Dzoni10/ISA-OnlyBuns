package rs.ac.uns.ftn.informatika.rest.dto;

import org.springframework.web.multipart.MultipartFile;
import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.time.LocalDateTime;

public class PostDTO {
    public int id;
    public int creatorId;
    public String description;
    public LocalDateTime creationTime;
    public LocationDTO location;
    public String imagePath;
    public String username;
    public MultipartFile imageFile;

    public PostDTO(int id, int creatorId, LocalDateTime creationTime, String description, LocationDTO location, String imagePath, String username) {
        this.creationTime = creationTime;
        this.description = description;
        this.id = id;
        this.creatorId = creatorId;
        this.location = location;
        this.imagePath = imagePath;
        this.username = username;
    }
    public PostDTO() {}

    public PostDTO(Post post) {
        this(post.getId(), post.getCreatorId(),post.getCreationTime(), post.getDescription(), new LocationDTO(post.getLocation()), post.getImagePath(), post.getUsername());
    }
}
