package com.sprint.mission.discodeit.domain.user.repository;

import com.sprint.mission.discodeit.config.TestJpaConfig;
import com.sprint.mission.discodeit.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestJpaConfig.class)
@DisplayName("UserRepository Slice Test")
class UserRepositoryTest
{
    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User test1 = userRepository.save(new User("test1", "passwordTest1", "test1@naver.com", null));
        User test2 = userRepository.save(new User("test2", "passwordTest2", "test2@naver.com", null));
    }

    @Test
    @DisplayName("유효한 정보로 유저 객체를 저장하면 ID가 생성된다")
    void save_WithValidInfo_AssignsId() {
        // given
        User test = new User("test3", "passwordTest3", null, null);

        // when
        User saved = userRepository.save(test);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("존재하는 사용자명으로 조회하면 생성된 유저 객체의 Username을 반환한다")
    void findByEmail_ExistingEmail_ReturnsUsername() {
        // given


        // when
        Optional<User> res = userRepository.findByUsername("test1");

        // then
        assertThat(res).isPresent().map(User::getUsername).contains("test1");
    }

    @Test
    @DisplayName("존재하지 않는 사용자명으로 조회하면 빈 값이 반환된다")
    void findByUsername_NonExistingUsername_ReturnsNull() {
        // given


        // when
        Optional<User> res = userRepository.findByUsername("xxzeroeight");

        // then
        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("모든 사용자 객체의 개수를 반환한다")
    void findAll_ExistingUser_RetuensUserCounts() {
        // given


        // when
        List<User> res = userRepository.findAll();

        // then
        assertThat(res).hasSize(2);
        assertThat(res).extracting(User::getUsername).contains("test1", "test2");
    }
}