package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "\"User\"")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "email")
    private String email;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "has_profile_picture")
    private Boolean hasProfilePicture;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasProfilePicture() {
        return hasProfilePicture;
    }

    public void setHasProfilePicture(Boolean hasProfilePicture) {
        this.hasProfilePicture = hasProfilePicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(hasProfilePicture, user.hasProfilePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, hasProfilePicture);
    }
}
