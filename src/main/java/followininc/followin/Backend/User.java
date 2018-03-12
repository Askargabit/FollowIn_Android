package followininc.followin.Backend;

/**
 * Created by ASKAR on 4-Feb-18.
 */

public class User {
    String id;
    String username;
    String profile_picture;
    Integer followers;
    Integer followings;

    public User(String id, String username, String profile_picture, Integer followers, Integer followings, Integer posts) {
        this.id = id;
        this.username = username;
        this.profile_picture = profile_picture;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
    }

    Integer posts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowings() {
        return followings;
    }

    public void setFollowings(Integer followings) {
        this.followings = followings;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }
}
