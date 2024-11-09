package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Post;

import java.time.LocalDateTime;

public class PostDTO {
    public int id;
    public int creatorId;
    public String description;
    public LocalDateTime creationTime;

    public PostDTO(int id, int creatorId, LocalDateTime creationTime, String description) {
        this.creationTime = creationTime;
        this.description = description;
        this.id = id;
        this.creatorId = creatorId;
    }
    public PostDTO() {}

    public PostDTO(Post post) {
        this(post.getId(), post.getCreatorId(),post.getCreationTime(), post.getDescription());
    }
}
