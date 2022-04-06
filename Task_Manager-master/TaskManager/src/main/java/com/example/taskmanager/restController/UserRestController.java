package com.example.taskmanager.restController;

import com.example.taskmanager.Entity.User;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.models.AuthenticationRequest;
import com.example.taskmanager.models.AuthenticationResponse;
import com.example.taskmanager.service.UserServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api")
public class UserRestController {

    private UserServiceImplementation userServiceImplementation;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    public UserRestController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }

    // expose "/users" and return list of users
    @GetMapping("/users")
    public List<User> findAll() {
        return userServiceImplementation.findAll();
    }

    // add mapping for GET /users/{userId}

    @GetMapping("/users/{userId}")
    public User getUser(@PathVariable Long userId) {

        User theUser = (User) userServiceImplementation.findById(userId);

        return theUser;
    }

    //Adding Post Mapping to add new user
    @PostMapping("/register")
    public User createNewUser(@RequestBody User newUser)  {
        LOGGER.info("A create user request initialized ");
        LOGGER.trace("Creating new user ");
        newUser.setId(0L);

        userServiceImplementation.save(newUser);
        return newUser;
    }

    //add mapping fot login authentication
    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws BadCredentialsException {
        return  userServiceImplementation.createAuthenticationToken(authenticationRequest);
    }


    // add mapping for PUT /users - update existing users

    @PutMapping("/users")
    public User updateUser(@RequestBody User theUser) {

        userServiceImplementation.save(theUser);

        return theUser;
    }

    // add mapping for DELETE /users/{UserId} - delete User

    @DeleteMapping("/users/{userId}")
    public String deleteEmployee(@PathVariable Long userId) {
        LOGGER.info("One user is deleted");

        User tempUser = (User) userServiceImplementation.findById(userId);

        // throw exception if null

        if (tempUser == null) {
            throw new NotFoundException("User id not found - " + userId);
        }

        userServiceImplementation.deleteById(userId);

        return "Deleted User id - " + userId;
    }





}
