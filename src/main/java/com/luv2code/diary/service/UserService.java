package com.luv2code.diary.service;

import com.luv2code.diary.domain.User;

import java.util.List;

public interface UserService {

    User save(final User user);

    User findById(final Long id);

    User findByUsername(final String username);

    List<User> findAll();

    User changeStatus(final User user);

    User update(final User oldUser, final User newUser);

    void delete(final User user);

}
