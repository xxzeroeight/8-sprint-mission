package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService
{
    private static JCFUserService instance;
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserService() {}

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }

        return instance;
    }

    @Override
    public User create(String username, String password, String email) {
        User user = new User(username, password, email);
        data.put(user.getId(), user);

        return user;
    }

    @Override
    public User findById(UUID id) {
        User user = data.get(id);

        return Optional.ofNullable(user)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String username, String password, String email) {
        User oldUser = data.get(id);

        User user = Optional.ofNullable(oldUser)
                .orElseThrow(() -> new UserException.UserNotFoundException(id));

        oldUser.update(username, password, email);

        return oldUser;
    }

    @Override
    public void delete(UUID id) {
        if (!data.containsKey(id)) {
            throw new UserException.UserNotFoundException(id);
        }

        data.remove(id);
    }
}
