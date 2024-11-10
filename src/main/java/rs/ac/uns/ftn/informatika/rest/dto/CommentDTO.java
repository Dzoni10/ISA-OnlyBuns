package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    public int id;
    public int postId;
    public String username;
    public LocalDateTime creationTime;
    public String text;

    public CommentDTO(int id, int postId, String username, LocalDateTime creationTime, String text) {
        this.id = id;
        this.postId = postId;
        this.username = username;
        this.creationTime = creationTime;
        this.text = text;
    }

    public CommentDTO() {
    }

    public CommentDTO(Comment comment) {
        this(comment.getId(), comment.getPostId(), comment.getUsername(), comment.getCreationTime(), comment.getText());
    }
}
