package rs.ac.uns.ftn.informatika.rest.domain;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "postId", nullable = false)
    private int postId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "creationTime", nullable = false)
    private LocalDateTime creationTime;

    @Column(name="text", nullable = false)
    private String text;
    public Comment() {
        super();
    }

    public Comment(int postId, String username, String text) {
        this.postId = postId;
        this.username = username;
        this.text = text;
        this.creationTime = LocalDateTime.now();

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Comment(CommentDTO commentDTO) {
        this.id = commentDTO.id;
        this.postId = commentDTO.postId;
        this.username = commentDTO.username;
        this.text = commentDTO.text;
        this.creationTime = commentDTO.creationTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", postId=" + postId +
                ", username='" + username + '\'' +
                ", creationTime=" + creationTime +
                '}';
    }
}
