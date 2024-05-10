package at.ac.tuwien.sepr.groupphase.backend.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "User_Weekly_Planner", schema = "PUBLIC", catalog = "DB")
public class UserWeeklyPlanner {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_planner_id", nullable = false)
    private WeeklyPlanner weeklyPlanner;

    public WeeklyPlanner getWeeklyPlanner() {
        return weeklyPlanner;
    }

    public void setWeeklyPlanner(WeeklyPlanner weeklyPlanner) {
        this.weeklyPlanner = weeklyPlanner;
    }

    @Basic
    @Column(name = "permission")
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
        return Objects.equals(user, that.user) && Objects.equals(weeklyPlanner, that.weeklyPlanner) && Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, weeklyPlanner, permission);
    }
}
