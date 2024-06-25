package at.ac.tuwien.sepr.groupphase.backend.service;

import at.ac.tuwien.sepr.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepr.groupphase.backend.entity.Recipe;

import java.util.List;

/**
 * This is the interface for the badgeService.
 */
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

    /**
     * Returns all badges of the given user.
     *
     * @param userId the user to get the badges from.
     * @return a list of string that represent the names of the badges a user has.
     */
    List<String> getBadgesOfUser(Long userId);
}
