package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication
{
    static User setUpUser(UserService userService) {
        User user = userService.create(new UserCreateRequest("woody", "woody1234", "woody@codeit.com"), Optional.of(
                new BinaryContentCreateRequest("test.txt", "text/plain", "hello".getBytes())
        ));
        System.out.println(user.toString());

        return user;
    }

    static Channel setUpPublicChannel(ChannelService channelService) {
        Channel channel = channelService.create(new PublicChannelCreateRequest("공지", "공지 채널입니다."));
        System.out.println(channel.toString());

        return channel;
    }

    static Channel setUpPrivateChannel(ChannelService channelService, List<UUID> userIds) {
        Channel channel = channelService.create(new PrivateChannelCreateRequest(userIds));
        System.out.println(channel.toString());

        return channel;
    }

    static void messageCreateTest(MessageService messageService, User user, Channel channel) {
        Message message = messageService.create(new MessageCreateRequest(channel.getId(), user.getId(), "안녕하세요."), List.of(
                new BinaryContentCreateRequest("f1.txt", "text/plain", "1".getBytes()),
                new BinaryContentCreateRequest("f2.txt", "text/plain", "2".getBytes())
        ));
        System.out.println(message.toString());

        System.out.println("메시지 작성: " + message.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        System.out.println("\n-----------------------Spring Bean Test-----------------------");
        UserService fileRepositoryUserService = context.getBean("basicUserService", UserService.class);
        ChannelService fileRepositoryChannelService = context.getBean("basicChannelService", ChannelService.class);
        MessageService fileRepositoryMessageService = context.getBean("basicMessageService", MessageService.class);

        User fileUser = setUpUser(fileRepositoryUserService);
        Channel filePublicChannel = setUpPublicChannel(fileRepositoryChannelService);
        Channel filePrivateChannel = setUpPrivateChannel(fileRepositoryChannelService, List.of(fileUser.getId()));
        messageCreateTest(fileRepositoryMessageService, fileUser, filePublicChannel);
    }
}
