package com.sprint.mission.discodeit.domain.message.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.user.domain.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Message extends BaseUpdatableEntity
{
    private String content;

    private final Channel channel;
    private final User author;
    private final List<BinaryContent> attachments = new ArrayList<>();

    // User, Channel이 있어야 존재가능.
    public Message(Channel channel, User author, String content) {
        this.channel = channel;
        this.author = author;
        this.content = content;
    }

    public void update(String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void addAttachment(BinaryContent attachment) {
        this.attachments.add(attachment);
    }

    public void removeAttachment(BinaryContent attachment) {
        this.attachments.remove(attachment);
    }
}
