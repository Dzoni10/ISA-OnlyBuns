package rs.ac.uns.ftn.informatika.rest.domain;

import rs.ac.uns.ftn.informatika.rest.dto.FollowingDTO;
import rs.ac.uns.ftn.informatika.rest.dto.PostDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "following")
public class Following {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "following", nullable = false)
    private String following;

    @Column(name = "creationTime", nullable = false)
    private LocalDateTime creationTime;

    public Following() {
        super();
    }

    public Following(String username, String following,LocalDateTime creationTime) {
        this.username = username;
        this.following = following;
        this.creationTime = creationTime;
    }

    public Following(FollowingDTO following){
        this.id = following.id;
        this.username = following.username;
        this.following = following.following;
        this.creationTime = following.creationTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Following{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", following='" + following + '\'' +
                ", creationTime='" + creationTime + '\'' +
                '}';
    }
}
