package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.exception.EntityAlreadyExistException;
import com.luv2code.diary.exception.EntityNotFoundException;
import com.luv2code.diary.exception.UserNotActiveException;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.luv2code.diary.domain.enums.UserStatus.ACTIVE;
import static com.luv2code.diary.domain.enums.UserStatus.INACTIVE;

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
        if (!doesUsernameAlreadyExists(user)) {
            setupVariablesCreate(user);
            LOGGER.info("Successfully setup variables for User with username: ´{}´.", user.getUsername());

            final User newUser = userRepository.save(user);
            LOGGER.info("Creating User with id: ´{}´.", user.getId());
            return newUser;
        } else {
            LOGGER.error("Username already exists for User with username: ´{}´.", user.getUsername());
            throw new EntityAlreadyExistException("User", "username", user.getUsername());
        }
    }

    private boolean doesUsernameAlreadyExists(final User searchedUser) {
        final List<User> users = findAll();
        LOGGER.info("Successfully founded all Users.");
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(searchedUser.getUsername()));
    }

    private void setupVariablesCreate(final User user) {
        LOGGER.info("Setting up variables for User with username: ´{}´.", user.getUsername());
        user.setNumberOfNotes(0);
        user.setStatus(ACTIVE);
    }

    @Override
    public User findById(final Long id) {
        final Optional<User> searchedUser = userRepository.findById(id);
        if (searchedUser.isPresent()) {
            LOGGER.info("Searching User with id: ´{}´.", id);
            return searchedUser.get();
        } else {
            LOGGER.error("Not founded User with id: ´{}´.", id);
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
            LOGGER.error("Not founded User with username: ´{}´.", username);
            throw new EntityNotFoundException("User", "username", username);
        }
    }

    @Override
    public List<User> findAll() {
        final List<User> users = userRepository.findAll();
        LOGGER.info("Searching all Users.");

        Collections.sort(users);
        LOGGER.info("Sorting all Users by username.");
        return users;
    }

    // TODO: @lzugaj - Send mail notification to user when admin change active state of user(disable/enable)
    @Override
    public User changeStatus(final User user) {
        if (user.getStatus().equals(ACTIVE)) {
            LOGGER.info("Set status to ´{}´ for User with id: ´{}´.", ACTIVE, user.getId());
            user.setStatus(INACTIVE);
        } else {
            LOGGER.info("Set status ´{}´ for User with id: ´{}´.", INACTIVE, user.getId());
            user.setStatus(ACTIVE);
        }

        return user;
    }

    @Override
    public User update(final User oldUser, final User newUser) {
        if (oldUser.getStatus().equals(ACTIVE)) {
            setupVariablesUpdate(oldUser, newUser);
            LOGGER.info("Successfully setup variables for User with id: ´{}´.", oldUser.getId());

            userRepository.save(oldUser);
            LOGGER.info("Updating User with id: ´{}´.", oldUser.getId());
            return oldUser;
        } else {
            LOGGER.error("Username already exists for User with id: ´{}´.", oldUser.getId());
            throw new UserNotActiveException("User", "id", String.valueOf(oldUser.getId()));
        }
    }

    private void setupVariablesUpdate(final User oldUser, final User newUser) {
        LOGGER.info("Setting up variables for User with id: ´{}´.", oldUser.getId());
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setEmail(newUser.getEmail());
    }

    @Override
    public void delete(final User user) {
        LOGGER.info("Deleting User with id: ´{}´.", user.getId());
        userRepository.delete(user);
    }
}
