package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;


import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ApplicationUser")
public class ApplicationUser {
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationUser user = (ApplicationUser) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(hasProfilePicture, user.hasProfilePicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, hasProfilePicture);
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserRole> userRoles;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<RecipeBook> recipeBooks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserRecipeBook> userRecipeBooks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<RecipeVerified> recipesVerified;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Favorite> favorites;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Cooked> cooked;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<Recipe> recipes;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<WeeklyPlanner> weeklyPlanners;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserWeeklyPlanner> userWeeklyPlanners;


    public boolean getAdmin() {
        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                if (role.getId() == 1) {
                    return true;
                }
            }
        }
        return false;
    }


    public static final class ApplicationUserBuilder {
        private long id;
        private String username;
        private String email;
        private String password;
        private Boolean hasProfilePicture;

        public ApplicationUserBuilder withid(long id) {
            this.id = id;
            return this;
        }

        public ApplicationUserBuilder withusername(String username) {
            this.username = username;
            return this;
        }

        public ApplicationUserBuilder withemail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserBuilder withpassword(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUserBuilder withhasProfilePicture(Boolean hasProfilePicture) {
            this.hasProfilePicture = hasProfilePicture;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser user = new ApplicationUser();
            user.setId(this.id);
            user.setUsername(this.username);
            user.setEmail(this.email);
            user.setPassword(this.password);
            user.setHasProfilePicture(this.hasProfilePicture);
            return user;
        }
    }
}
