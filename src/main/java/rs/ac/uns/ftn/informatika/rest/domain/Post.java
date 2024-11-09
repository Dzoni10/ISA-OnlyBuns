package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="app-post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="creatorId",nullable = false)
    private int creatorId;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="creationTime",nullable = false)
    private LocalDateTime creationTime;

    public Post() {
        super();
    }
    public Post(int creatorId, String description, LocalDateTime creationTime) {
        super();
        this.creatorId = creatorId;
        this.description = description;
        this.creationTime = creationTime;
    }

    public Post(PostDTO post){
        this.id = post.id;
        this.creatorId = post.creatorId;
        this.description = post.description;
        this.creationTime = post.creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }
}
