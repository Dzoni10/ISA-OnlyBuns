package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="app_post")
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

    @Embedded
    private Location location;

    @Column(name="imagePath",nullable = false)
    private String imagePath;
    @Column(name="username",nullable = false)
    private String username;

    public Post() {
        super();
    }
    public Post(int creatorId, String description, Location location, String imagePath,String username) {
        super();
        this.creatorId = creatorId;
        this.description = description;
        this.creationTime = LocalDateTime.now();
        this.location = location;
        this.imagePath = imagePath;
        this.username=username;
    }

    public Post(PostDTO post){
        this.id = post.id;
        this.creatorId = post.creatorId;
        this.description = post.description;
        this.creationTime = post.creationTime;
        this.username = post.username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
