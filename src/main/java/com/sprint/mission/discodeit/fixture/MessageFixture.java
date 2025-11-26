package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class MessageFixture
{
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
}
