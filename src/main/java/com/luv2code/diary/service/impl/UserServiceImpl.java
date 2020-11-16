package com.luv2code.diary.service.impl;

import com.luv2code.diary.domain.User;
import com.luv2code.diary.repository.UserRepository;
import com.luv2code.diary.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        setupVariables(user);

        final User newUser = userRepository.save(user);
        LOGGER.info("Saving new User with id: ´{}´.", user.getId());
        return newUser;
    }

    private void setupVariables(final User user) {
        user.setNumberOfNotes(0);
        user.setIsActive(true);
    }

    @Override
    public User findById(Long id) {
        final User searchedUser = userRepository.findById(id).orElse(null);
        LOGGER.info("Searching User with id: ´{}´.", id);
        return searchedUser;
    }

    @Override
    public List<User> findAll() {
        final List<User> users = userRepository.findAll();
        LOGGER.info("Searching all Users.");
        return users;
    }

    @Override
    public List<User> findAllEnabled() {
        final List<User> users = findAll();
        LOGGER.info("Searching all enabled Users.");
        return users.stream()
                .filter(User::getIsActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findAllDisabled() {
        final List<User> users = findAll();
        LOGGER.info("Searching all disabled Users.");
        return users.stream()
                .filter(user -> !user.getIsActive())
                .collect(Collectors.toList());
    }

    @Override
    public User changeActiveState(final Long id) {
        final User searchedUser = findById(id);
        if (isActive(searchedUser)) {
            searchedUser.setIsActive(false);
            LOGGER.info("Disabling User with id: ´{}´.", id);
        } else {
            searchedUser.setIsActive(true);
            LOGGER.info("Enabling User with id: ´{}´.", id);
        }

        return searchedUser;
    }

    private boolean isActive(final User user) {
        return user.getIsActive();
    }
}
