package com.sprint.mission.discodeit.domain.user.repository;

import com.sprint.mission.discodeit.domain.user.domain.Role;
import com.sprint.mission.discodeit.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByUsername(String username);

    List<User> findAll();

    List<User> findAllByRole(Role role);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    boolean existsByRole(Role role);
}
