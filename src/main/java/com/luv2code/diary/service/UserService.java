package com.luv2code.diary.service;

import com.luv2code.diary.domain.User;

import java.util.List;

public interface UserService {

    User save(final User user);

    User findById(final Long id);

    List<User> findAll();

    List<User> findAllEnabled();

    List<User> findAllDisabled();

    User changeActiveState(final Long id);

}
