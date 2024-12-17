package rs.ac.uns.ftn.informatika.rest.dto;

import rs.ac.uns.ftn.informatika.rest.domain.Following;

import java.time.LocalDateTime;

public class FollowingDTO {
    public int id;
    public String username;
    public String following;
    public LocalDateTime creationTime;

    public FollowingDTO(int id, String username, String following,LocalDateTime creationTime) {
        this.id = id;
        this.username = username;
        this.following = following;
        this.creationTime = creationTime;
    }
    public FollowingDTO() {}

    public FollowingDTO(Following followingEntity) {
        this(followingEntity.getId(), followingEntity.getUsername(), followingEntity.getFollowing(), followingEntity.getCreationTime());
    }
}
