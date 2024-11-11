package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Comment;
import rs.ac.uns.ftn.informatika.rest.domain.Like;

import java.time.LocalDateTime;

public class LikeDTO {
    public int id;
    public int postId;
    public String username;

    public LikeDTO(int id, int postId, String username) {
        this.id = id;
        this.postId = postId;
        this.username = username;
    }

    public LikeDTO() {
    }

    public LikeDTO(Like like) {
        this(like.getId(), like.getPostId(), like.getUsername());
    }
}
