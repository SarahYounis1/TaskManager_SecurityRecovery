package com.example.taskmanager.service;

import com.example.taskmanager.Entity.Tokens;
import com.example.taskmanager.Entity.User;
import com.example.taskmanager.daoRepository.TaskRepository;
import com.example.taskmanager.daoRepository.TokenRepository;
import com.example.taskmanager.daoRepository.UserRepository;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.models.AuthenticationRequest;
import com.example.taskmanager.models.AuthenticationResponse;
import com.example.taskmanager.security.JWTSecurity.JwtUtil;
import com.example.taskmanager.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplementation implements CrudService<User> {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtTokenUtil;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserServiceImplementation(UserDetailsServiceImpl userDetailsService, JwtUtil jwtTokenUtil, TaskRepository taskRepository,
                                     UserRepository theUserRepository, TokenRepository tokenRepository,
                                     AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.taskRepository = taskRepository;
        userRepository = theUserRepository;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User findById(Long theId) {
        Optional<User> result = userRepository.findById(theId);

        User theUser = null;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            // we didn't find the user
            throw new NotFoundException("Did not find User id - " + theId);
        }

        return theUser;
    }

    @Override
    @Transactional
    public void save(User theUser) {

        userRepository.save(theUser);
    }

    @Override
    @Transactional
    public void deleteById(Long theId) {

        userRepository.deleteById(theId);
    }


    public AuthenticationResponse createAuthenticationToken(AuthenticationRequest authenticationRequest) {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {

            throw new BadCredentialsException("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        Tokens token = new Tokens();
        User user=(User)userDetails;
        token.setUser(user);
        token.setJwtToken(jwt);
        tokenRepository.save(token);
        user.addToken(token);
        userRepository.save(user);
        return new AuthenticationResponse(jwt);
    }
}