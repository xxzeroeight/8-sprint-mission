package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService
{
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String password, String email) {
        return userRepository.save(new User(username, password, email));
    }

    @Override
    public User findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));

        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String username, String password, String email) {
        if (id == null) {
            return null;
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));

        user.update(username, password, email);

        return userRepository.save(user);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            return;
        }

        userRepository.delete(id);
    }
}
