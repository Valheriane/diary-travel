package com.japanApply.journal.service;

import com.japanApply.journal.model.Ownable;
import com.japanApply.journal.model.RangType;
import com.japanApply.journal.model.User;
import com.japanApply.journal.security.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    /**
     * Checks if the current user can access a resource owned by a user.
     */
    public boolean canAccessResource(Object resource) {
        if (resource == null) {
            return false;
        }

        String currentUsername = authenticationFacade.getCurrentUsername();
        Optional<User> currentUser = userService.getByUsername(currentUsername);

        if (currentUser.isPresent()) {
            System.out.println("Current user: " + currentUser.get().getEmail());  // Debug log

            // If the resource is a user, check if it's the same user
            if (resource instanceof User) {
                User user = (User) resource;
                // Check if the current user can access this user profile
                return canAccessUser(user);
            }

            // If the resource is an Ownable type
            if (resource instanceof Ownable) {
                Ownable ownableResource = (Ownable) resource;
                // Check if the user has the ADMINISTRATOR role
                if (currentUser.get().getRangType() == RangType.ADMINISTRATOR) {
                    return true;
                }

                // Check if the user is the owner of the resource
                // Correct comparison of long IDs using '=='
                return ownableResource.getOwner().getId() == currentUser.get().getId();
            }
        }

        // If the user is not found, access is denied
        return false;
    }

    /**
     * Checks if the current user can access another user profile.
     */
    public boolean canAccessUser(User user) {
        if (user == null) {
            return false;
        }

        // Retrieve the current user
        String currentUsername = authenticationFacade.getCurrentUsername();
        Optional<User> currentUser = userService.getByUsername(currentUsername);

        if (currentUser.isPresent()) {
            // Check if the current user is an administrator
            if (currentUser.get().getRangType() == RangType.ADMINISTRATOR) {
                return true; // Administrators can access all users
            }

            // Using '==' to compare primitive long types
            return user.getId() == currentUser.get().getId();
        }

        return false; // If the current user is not found, access is denied
    }
}
