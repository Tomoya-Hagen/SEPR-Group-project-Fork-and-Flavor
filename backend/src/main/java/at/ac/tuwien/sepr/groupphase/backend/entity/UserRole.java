package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "User_Role", schema = "PUBLIC", catalog = "DB")
@IdClass(Userrolepk.class)
public class UserRole {
    @Id
    @Column(name = "user_id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "role_id")
    private long roleId;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserRole userRole = (UserRole) o;
        return Objects.equals(userId, userRole.userId) && Objects.equals(roleId, userRole.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }

    public static final class UserRoleBuilder {
        private long userId;
        private long roleId;

        public UserRoleBuilder() {
        }

        public UserRoleBuilder withuserId(long id) {
            this.userId = id;
            return this;
        }

        public UserRoleBuilder withroleId(long id) {
            this.roleId = id;
            return this;
        }


        public UserRole build() {
            UserRole userRole = new UserRole();
            userRole.setRoleId(this.roleId);
            userRole.setUserId(this.userId);
            return userRole;
        }
    }
}
