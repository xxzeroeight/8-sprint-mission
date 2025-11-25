package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication
{
    public static void main(String[] args) {
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();

        System.out.println("-----------------------유저-----------------------");
        User user1 = userService.create("user1", "user1Password", "user1@naver.com");
        User user2 = userService.create("user2", "user2Password", "user2@naver.com");
        User user3 = userService.create("user3", "user3Password", "user3@naver.com");

        System.out.println("[등록]");
        System.out.println("Created user1: " + user1);
        System.out.println("Created user2: " + user2);
        System.out.println("Created user3: " + user3);

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

        System.out.println("\n-----------------------채널-----------------------");
        Channel channel1 = channelService.create("channel1", "channel1입니다.", ChannelType.PUBLIC);
        Channel channel2 = channelService.create("channel2", "channel2입니다.", ChannelType.PRIVATE);
        Channel channel3 = channelService.create("channel3", "channel3입니다.", ChannelType.PRIVATE);

        System.out.println("[등록]");
        System.out.println("Created channel1: " + channel1);
        System.out.println("Created channel2: " + channel2);
        System.out.println("Created channel3: " + channel3);

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

        System.out.println("\n-----------------------메시지-----------------------");
        Message message1 = messageService.create("message1", user2.getId(), channel2.getId());
        Message message2 = messageService.create("message1", user3.getId(), channel3.getId());

        System.out.println("[등록]");
        System.out.println("Created message1: " + message1);
        System.out.println("Created message2: " + message2);

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

        System.out.println("\n-----------------------File IO를 통한 데이터 영속화-----------------------");

        UserService fileUserService = FileUserService.getInstance();
        ChannelService fileChannelService = FileChannelService.getInstance();
        MessageService fileMessageService = FileMessageService.getInstance();

        System.out.println("-----------------------유저-----------------------");
        User fileUser1 = fileUserService.create("fileUser1", "fileUser1Password", "fileUser1@naver.com");
        User fileUser2 = fileUserService.create("fileUser2", "fileUser2Password", "fileUser2@naver.com");
        User fileUser3 = fileUserService.create("fileUser3", "fileUser3Password", "fileUser3@naver.com");

        System.out.println("[등록]");
        System.out.println("Created fileUser1: " + fileUser1);
        System.out.println("Created fileUser2: " + fileUser2);
        System.out.println("Created fileUser3: " + fileUser3);
        System.out.println("Total Users: " + fileUserService.findAllUsers().size());

        System.out.println("[조회(단건)]");
        User findFileUser1 = fileUserService.findById(fileUser1.getId());
        System.out.println("Found fileUser1: " + findFileUser1);

        System.out.println("[조회(다건)]");
        List<User> findAllFileUsers = fileUserService.findAllUsers();
        System.out.println("Found all fileUsers: " + findAllFileUsers);

        System.out.println("[수정 & 수정된 데이터 조회]");
        User updatedFileUser1 = fileUserService.update(fileUser1.getId(), "updatedFileUser1", "updatedFileUser1Password", "updatedFileUser1@naver.com");
        System.out.println("Updated fileUser1: " + updatedFileUser1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        fileUserService.delete(fileUser1.getId());
        User deletedFileUser1 = fileUserService.findById(fileUser1.getId());
        System.out.println("Deleted fileUser1: " + deletedFileUser1);
        System.out.println("Total Users: " + userService.findAllUsers().size());

        System.out.println("\n-----------------------채널-----------------------");
        Channel fileChannel1 = fileChannelService.create("fileChannel1", "fileChannel1", ChannelType.PUBLIC);
        Channel fileChannel2 = fileChannelService.create("fileChannel2", "fileChannel2", ChannelType.PRIVATE);
        Channel fileChannel3 = fileChannelService.create("fileChannel3", "fileChannel3", ChannelType.PRIVATE);

        System.out.println("[등록]");
        System.out.println("Created fileChannel1: " + fileChannel1);
        System.out.println("Created fileChannel2: " + fileChannel2);
        System.out.println("Created fileChannel3: " + fileChannel3);
        System.out.println("Totla Channels: " + fileChannelService.findAllChannels().size());

        System.out.println("[조회(단건)]");
        Channel findFileChannel1 = fileChannelService.findById(fileChannel1.getId());
        System.out.println("Found fileChannel1: " + findFileChannel1);

        System.out.println("[조회(다건)]");
        List<Channel> findAllFileChannels = fileChannelService.findAllChannels();
        System.out.println("Found all fileChannels: " + findAllFileChannels);

        System.out.println("[수정 & 수정된 데이터 조회]");
        Channel updatedFileChannel1 = fileChannelService.update(fileChannel1.getId(), "updatedFileChannel1", "upudatedFileChannel1Password", ChannelType.PRIVATE);
        System.out.println("Updated fileChannel1: " + updatedFileChannel1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        fileChannelService.delete(fileChannel1.getId());
        Channel deletedFileChannel1 = fileChannelService.findById(fileChannel1.getId());
        System.out.println("Deleted fileChannel1: " + deletedFileChannel1);
        System.out.println("Totla Channels: " + fileChannelService.findAllChannels().size());

        System.out.println("\n-----------------------메시지-----------------------");
        Message fileMessage1 = fileMessageService.create("fileMessage1", fileUser2.getId(), fileChannel2.getId());
        Message fileMessage2 = fileMessageService.create("fileMessage2", fileUser3.getId(), fileChannel3.getId());

        System.out.println("[등록]");
        System.out.println("Created fileMessage1: " + fileMessage1);
        System.out.println("Created fileMessage2: " + fileMessage2);
        System.out.println("Totla Messages: " + fileMessageService.findAllMessages().size());

        System.out.println("[조회(단건)]");
        Message findFileMessage1 = fileMessageService.findById(fileMessage1.getId());
        System.out.println("Found fileMessage1: " + findFileMessage1);

        System.out.println("[조회(다건)");
        List<Message> findAllFileMessages = fileMessageService.findAllMessages();
        System.out.println("Found all fileMessages: " + findAllFileMessages);

        System.out.println("[수정 & 수정된 데이터 조회]");
        Message updatedFileMessage1 = fileMessageService.update(fileMessage1.getId(), "updatedFileMessage1");
        System.out.println("Updated fileMessage1: " + updatedFileMessage1);

        System.out.println("[삭제 & 조회를 통해 삭제되었는지 확인]");
        fileMessageService.delete(fileMessage1.getId());
        Message deletedFileMessage1 = fileMessageService.findById(fileMessage1.getId());
        System.out.println("Deleted fileMessage1: " + deletedFileMessage1);
        System.out.println("Totla Messages: " + fileMessageService.findAllMessages().size());

        /* JCF*Service VS File*Service의 "비즈니스 로직 차이점 */

    }
}
