package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class UserFixture
{
    public static void userCRUDTest(UserService userService) {
        System.out.println("-----------------------User CRUD Test-----------------------");
        User user1 = userService.create("user1", "user1Password", "user1@naver.com");
        User user2 = userService.create("user2", "user2Password", "user2@naver.com");
        User user3 = userService.create("user3", "user3Password", "user3@naver.com");

        System.out.println("[등록]");
        System.out.println("Created user1: " + user1);
        System.out.println("Created user2: " + user2);
        System.out.println("Created user3: " + user3);
        System.out.println("Total Users: " + userService.findAllUsers().size());

        System.out.println("[조회(단건)]");
        User findUser1 = userService.findById(user1.getId());
        System.out.println("Found user1: " + findUser1);

        System.out.println("[조회(다건)]");
        List<User> findAllUsers = userService.findAllUsers();
        System.out.println("Found all users: " + findAllUsers);

        System.out.println("[수정 & 수정된 데이터 조회]");
        User updatedUser1 = userService.update(user1.getId(), "xxzeroeight", "password", "xxzeroeight@naver.com");
        System.out.println("Updated user: " + updatedUser1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        userService.delete(user1.getId());
        List<User> allUsers = userService.findAllUsers();
        System.out.println("Found all users: " + allUsers);
        System.out.println("Total Users: " + userService.findAllUsers().size());
    }
}
