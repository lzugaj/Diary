package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityAlreadyExistException;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(final User user) {
        if (!isUsernameAlreadyExists(user)) {
            setupVariables(user);
            LOGGER.info("Successfully setup variables for User with username: ´{}´", user.getUsername());

            final User newUser = userRepository.save(user);
            LOGGER.info("Saving new User with id: ´{}´.", user.getId());
            return newUser;
        } else {
            LOGGER.error("User already exists with username: ´{}´.", user.getUsername());
            throw new EntityAlreadyExistException("User", "username", user.getUsername());
        }
    }

    private boolean isUsernameAlreadyExists(final User searchedUser) {
        final List<User> users = findAll();
        LOGGER.info("Successfully founded all Users.");
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(searchedUser.getUsername()));
    }

    private void setupVariables(final User user) {
        LOGGER.info("Setting up variables for User with username: ´{}´", user.getUsername());
        user.setNumberOfNotes(0);
        user.setIsActive(true);
    }

    @Override
    public User findById(final Long id) {
        final Optional<User> searchedUser = userRepository.findById(id);
        if (searchedUser.isPresent()) {
            LOGGER.info("Searching User with id: ´{}´.", id);
            return searchedUser.get();
        } else {
            LOGGER.error("User not founded with id: ´{}´.", id);
            throw new EntityNotFoundException("User", "id", String.valueOf(id));
        }
    }

    @Override
    public User findByUsername(final String username) {
        final Optional<User> searchedUser = userRepository.findByUsername(username);
        if (searchedUser.isPresent()) {
            LOGGER.info("Searching User with username: ´{}´.", username);
            return searchedUser.get();
        } else {
            LOGGER.error("User not founded with username: ´{}´.", username);
            throw new EntityNotFoundException("User", "username", username);
        }
    }

    @Override
    public List<User> findAll() {
        final List<User> users = userRepository.findAll();
        LOGGER.info("Searching all Users.");
        return users;
    }

    // TODO: @lzugaj - Send mail notification to user when admin change active state of user(disable/enable)
    @Override
    public User changeStatus(final User user) {
        if (user.getIsActive()) {
            LOGGER.info("Disabling User with id: ´{}´.", user.getId());
            user.setIsActive(false);
        } else {
            LOGGER.info("Enabling User with id: ´{}´.", user.getId());
            user.setIsActive(true);
        }

        return user;
    }
}
