package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.domain.user.domain.Role;
import com.sprint.mission.discodeit.domain.user.domain.User;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class AdminAccountInitializer implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_EMAIL;
    private final String ADMIN_USERNAME;
    private final String ADMIN_PASSWORD;

    public AdminAccountInitializer(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  @Value("${admin.email}") String ADMIN_EMAIL,
                                  @Value("${admin.username}") String ADMIN_USERNAME,
                                  @Value("${admin.password}") String ADMIN_PASSWORD)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.ADMIN_EMAIL = ADMIN_EMAIL;
        this.ADMIN_USERNAME = ADMIN_USERNAME;
        this.ADMIN_PASSWORD = ADMIN_PASSWORD;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // 어드민이 없는 경우에만 생성
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }

        User user = new User(
                ADMIN_USERNAME,
                passwordEncoder.encode(ADMIN_PASSWORD),
                ADMIN_EMAIL,
                null
        );

        user.updateRole(Role.ADMIN);
        userRepository.save(user);

        log.info("ADMIN 계정 생성 완료");
    }
}
