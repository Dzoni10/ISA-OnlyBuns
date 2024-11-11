package rs.ac.uns.ftn.informatika.rest.domain;
import rs.ac.uns.ftn.informatika.rest.dto.CommentDTO;
import rs.ac.uns.ftn.informatika.rest.dto.LikeDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_like")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "postId", nullable = false)
    private int postId;

    @Column(name = "username", nullable = false)
    private String username;

    public Like() {
        super();
    }

    public Like(int postId, String username) {
        this.postId = postId;
        this.username = username;
    }

    public Like(LikeDTO likeDTO) {
        this.id = likeDTO.id;
        this.postId = likeDTO.postId;
        this.username = likeDTO.username;
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

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", postId=" + postId +
                ", username='" + username + '\'' +
                '}';
    }
}

