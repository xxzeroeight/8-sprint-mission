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
    @DisplayName("성공: 유저 저장")
    void givenUser_whenSave_thenIdExists() {
        // given
        User test = new User("test3", "passwordTest3", null, null);

        // when
        User saved = userRepository.save(test);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("성공: 유저 조회")
    void givenUsername_whenFind_thenReturnsUser() {
        // given


        // when
        Optional<User> res = userRepository.findByUsername("test1");

        // then
        assertThat(res).isPresent().map(User::getUsername).contains("test1");
    }

    @Test
    @DisplayName("실패: 존재하지 않는 사용자명")
    void givenUsername_whenFind_thenUserDoesNotExist() {
        // given


        // when
        Optional<User> res = userRepository.findByUsername("xxzeroeight");

        // then
        assertThat(res).isEmpty();
    }

    @Test
    @DisplayName("성공: 모든 유저 조회")
    void given_whenFind_thenReturnsUsers() {
        // given


        // when
        List<User> res = userRepository.findAll();

        // then
        assertThat(res).hasSize(2);
        assertThat(res).extracting(User::getUsername).contains("test1", "test2");
    }
}