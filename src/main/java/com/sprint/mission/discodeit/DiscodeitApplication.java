package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication
{
    static User setUpUser(UserService userService) {
        return userService.create("woody", "woody1234", "woody@codeit.com");
    }

    static Channel setUpChannel(ChannelService channelService) {
        return channelService.create("공지", "공지 채널입니다.", ChannelType.PUBLIC);
    }

    static void messageCreateTest(MessageService messageService, User user, Channel channel) {
        Message m =  messageService.create("안녕하세요", user.getId(), channel.getId());
        System.out.println("메시지 생성: " + m.getId());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        System.out.println("\n-----------------------JCF Repository Test-----------------------");
        UserRepository jcfUserRepository = JCFUserRepository.getInstance();
        ChannelRepository jcfChannelRepository = JCFChannelRepository.getInstance();
        MessageRepository jcfMessageRepository = JCFMessageRepository.getInstance();

        UserService jcfRepositoryUserService = new BasicUserService(jcfUserRepository);
        ChannelService jcfRepositoryChannelService = new BasicChannelService(jcfChannelRepository);
        MessageService jcfRepositoryMessageService = new BasicMessageService(jcfMessageRepository);

        User jcfUser = setUpUser(jcfRepositoryUserService);
        Channel jcfChannel = setUpChannel(jcfRepositoryChannelService);
        messageCreateTest(jcfRepositoryMessageService, jcfUser, jcfChannel);

        System.out.println("\n-----------------------File Repository Test-----------------------");
        UserService fileRepositoryUserService = context.getBean("basicUserService", UserService.class);
        ChannelService fileRepositoryChannelService = context.getBean("basicChannelService", ChannelService.class);
        MessageService fileRepositoryMessageService = context.getBean("basicMessageService", MessageService.class);

        User fileUser = setUpUser(fileRepositoryUserService);
        Channel fileChannel = setUpChannel(fileRepositoryChannelService);
        messageCreateTest(fileRepositoryMessageService, fileUser, fileChannel);
    }
}
