package com.icwd.user.service.services;

import com.icwd.user.service.entities.User;

import java.util.List;

public interface UserService {
    //user operation

    // create

    User saveUser(User user);

    //get all users
    List<User> getAllUser();

    //get single user of give userId

    User getUser(String userId);

    // TODO : delete
    //TODO: update
}
