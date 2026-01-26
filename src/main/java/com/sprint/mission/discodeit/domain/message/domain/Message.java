package com.sprint.mission.discodeit.domain.message.domain;

import com.sprint.mission.discodeit.global.entity.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.channel.domain.Channel;
import com.sprint.mission.discodeit.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "messages")
@Entity
public class Message extends BaseUpdatableEntity
{
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_messages_channel"))
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_messages_author"))
    private User author;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<BinaryContent> attachments = new ArrayList<>();

    public Message(Channel channel, User author, String content, List<BinaryContent> attachments) {
        this.channel = channel;
        this.author = author;
        this.content = content;
        this.attachments = attachments;
    }

    public void update(String content) {
        if (content != null) {
            this.content = content;
        }
    }
}
