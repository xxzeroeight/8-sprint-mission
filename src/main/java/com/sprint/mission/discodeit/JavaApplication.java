package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication
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
        User deletedUser1 = userService.findById(user1.getId());
        System.out.println("Deleted user: " + deletedUser1);
        System.out.println("Total Users: " + userService.findAllUsers().size());
    }

    public static void channelCRUDTest(ChannelService channelService) {
        System.out.println("\n-----------------------Channel CRUD Test-----------------------");
        Channel channel1 = channelService.create("channel1", "channel1입니다.", ChannelType.PUBLIC);
        Channel channel2 = channelService.create("channel2", "channel2입니다.", ChannelType.PRIVATE);
        Channel channel3 = channelService.create("channel3", "channel3입니다.", ChannelType.PRIVATE);

        System.out.println("[등록]");
        System.out.println("Created channel1: " + channel1);
        System.out.println("Created channel2: " + channel2);
        System.out.println("Created channel3: " + channel3);
        System.out.println("Total Channels: " + channelService.findAllChannels().size());

        System.out.println("[조회(단건)]");
        Channel findChannel2 = channelService.findById(channel2.getId());
        System.out.println("Found channel2: " + findChannel2);

        System.out.println("[조회(다건)]");
        List<Channel> findAllChannels = channelService.findAllChannels();
        System.out.println("Found all channels: " + findAllChannels);

        System.out.println("[수정 & 수정된 데이터 조회]");
        Channel updatedChannel1 = channelService.update(channel1.getId(), "TestChannel", "테스트 채널입니다.", ChannelType.PUBLIC);
        System.out.println("Updated channel1: " + updatedChannel1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        channelService.delete(channel1.getId());
        Channel deletedChannel1 = channelService.findById(channel1.getId());
        System.out.println("Deleted channel1: " + deletedChannel1);
        System.out.println("Total Channels: " + channelService.findAllChannels().size());
    }

    public static void messageCRUDTest(MessageService messageService, UserService userService, ChannelService channelService) {
        User user1 = userService.create("user1", "user1Password", "user1@naver.com");
        User user2 = userService.create("user2", "user2Password", "user2@naver.com");
        User user3 = userService.create("user3", "user3Password", "user3@naver.com");

        Channel channel1 = channelService.create("channel1", "channel1입니다.", ChannelType.PUBLIC);
        Channel channel2 = channelService.create("channel2", "channel2입니다.", ChannelType.PRIVATE);
        Channel channel3 = channelService.create("channel3", "channel3입니다.", ChannelType.PRIVATE);

        System.out.println("\n-----------------------Message CRUD Test-----------------------");
        Message message1 = messageService.create("message1", user2.getId(), channel2.getId());
        Message message2 = messageService.create("message1", user3.getId(), channel3.getId());

        System.out.println("[등록]");
        System.out.println("Created message1: " + message1);
        System.out.println("Created message2: " + message2);
        System.out.println("Total Messages: " + messageService.findAllMessages().size());

        System.out.println("[조회(단건]");
        Message findMessage1 = messageService.findById(message1.getId());
        System.out.println("Found message1: " + findMessage1);

        System.out.println("[조회(다건)");
        List<Message> findAllMessages = messageService.findAllMessages();
        System.out.println("Found all messages: " + findAllMessages);

        System.out.println("[수정 & 수정된 데이터 조회]");
        Message updatedMessage1 = messageService.update(message1.getId(), "수정된 메시지.");
        System.out.println("Updated message2: " + updatedMessage1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        messageService.delete(message1.getId());
        Message deletedMessage1 = messageService.findById(message1.getId());
        System.out.println("Deleted message2: " + deletedMessage1);
        System.out.println("Total Messages: " + messageService.findAllMessages().size());

    }

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
        System.out.println("\n-----------------------In-Memory Test-----------------------");

        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();

        userCRUDTest(userService);
        channelCRUDTest(channelService);
        messageCRUDTest(messageService, userService, channelService);

        System.out.println("\n-----------------------File IO Test-----------------------");
        UserService fileUserService = FileUserService.getInstance();
        ChannelService fileChannelService = FileChannelService.getInstance();
        MessageService fileMessageService = FileMessageService.getInstance();

        userCRUDTest(fileUserService);
        channelCRUDTest(fileChannelService);
        messageCRUDTest(fileMessageService, fileUserService, fileChannelService);

        /* JCF*Service VS File*Service */
        // 공: "비즈니스 로직"과 "저장 로직"이 함께 존재.
        // 차: JCF*Service는 In-Memory(휘발), File*Service는 File에 저장(비휘발)

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
        UserRepository fileUserRepository = FileUserRepository.getInstance();
        ChannelRepository fileChannelRepository = FileChannelRepository.getInstance();
        MessageRepository fileMessageRepository = FileMessageRepository.getInstance();

        UserService fileRepositoryUserService = new BasicUserService(fileUserRepository);
        ChannelService fileRepositoryChannelService = new BasicChannelService(fileChannelRepository);
        MessageService fileRepositoryMessageService = new BasicMessageService(fileMessageRepository);

        User fileUser = setUpUser(fileRepositoryUserService);
        Channel fileChannel = setUpChannel(fileRepositoryChannelService);

        messageCreateTest(fileRepositoryMessageService, fileUser, fileChannel);
    }

    /* JCF*Service or File*Service와의 차이점 */
    // "비즈니스 로직"과 "저장 로직"이 확실하게 분리되어 코드 작성이나 유지보수에 좋음.
}
