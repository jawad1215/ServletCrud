package org.example.service;

import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {

    private final Map<Integer, User> usersDB = new ConcurrentHashMap<>();

    public User createUser(User userReq) {
        usersDB.put(userReq.getId(), userReq);
        return userReq;
    }

    public User getUserById(Integer id) {
        return usersDB.get(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersDB.values());
    }

    public User updateUser(Integer id, User userReq) {
        if (!usersDB.containsKey(id)) {
            return null;
        }
        userReq.setId(id);
        usersDB.put(id, userReq);
        return userReq;
    }

    public boolean deleteUser(Integer id) {
        return usersDB.remove(id) != null;
    }
}
