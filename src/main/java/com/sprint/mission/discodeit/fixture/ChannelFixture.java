package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;

public class ChannelFixture
{
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
}
