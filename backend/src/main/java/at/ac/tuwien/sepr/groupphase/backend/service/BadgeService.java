package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;

import java.util.List;

public interface BadgeService {
    /**
     * Gives the given user the Role Contributor.
     *
     * @param user that gets the new role.
     * @param role that will be added to the user if the user does not already have it.
     */
    void addRoleToUser(ApplicationUser user, Roles role);

    /** Returns all badges of the currently logged in user.
     *
     * @return a list of string that represent the names of the badges a user has.
     */
    List<String> getBadgesOfCurrentUser();
}
