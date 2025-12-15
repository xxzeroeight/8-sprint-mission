package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository
{
    private final Map<UUID, User> data = new HashMap<>();

    private JCFUserRepository() {}

    private static JCFUserRepository instance;
    public static JCFUserRepository getInstance() {
        if (instance == null) instance = new JCFUserRepository();
        return instance;
    }

    @Override
    public User save(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        User user = data.get(id);

        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return findAll().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
