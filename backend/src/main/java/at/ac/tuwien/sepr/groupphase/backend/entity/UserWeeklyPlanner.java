package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;
import java.util.Objects;

@Entity
@jakarta.persistence.Table(name = "User_Weekly_Planner", schema = "PUBLIC", catalog = "DB")
public class UserWeeklyPlanner {
    @Id
    @jakarta.persistence.Column(name = "user_id")
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Id
    @jakarta.persistence.Column(name = "weekly_planner_id")
    private long weeklyPlannerId;

    public long getWeeklyPlannerId() {
        return weeklyPlannerId;
    }

    public void setWeeklyPlannerId(long weeklyPlannerId) {
        this.weeklyPlannerId = weeklyPlannerId;
    }

    @Basic
    @jakarta.persistence.Column(name = "permission")
    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserWeeklyPlanner that = (UserWeeklyPlanner) o;
        return Objects.equals(userId, that.userId) && Objects.equals(weeklyPlannerId, that.weeklyPlannerId) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, weeklyPlannerId, permission);
    }
}
