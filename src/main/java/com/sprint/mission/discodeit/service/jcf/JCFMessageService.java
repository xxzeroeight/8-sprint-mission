package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService
{
    private static JCFMessageService instance;
    private final Map<UUID, Message> data = new HashMap<>();

    private final ChannelService channelService;
    private final UserService userService;

    private JCFMessageService() {
        this.channelService = JCFChannelService.getInstance();
        this.userService = JCFUserService.getInstance();
    }

    public static JCFMessageService getInstance() {
        if (instance == null) {
            instance = new JCFMessageService();
        }

        return instance;
    }

    @Override
    public Message create(String content, UUID authorId, UUID channelId) {
        if (channelService.findById(channelId) == null) {
            throw new IllegalArgumentException(String.format("Channel with id %s does not exist", channelId));
        }

        if (userService.findById(authorId) == null) {
            throw new IllegalArgumentException(String.format("Author with id %s does not exist", authorId));
        }

        Message message = new Message(channelId, authorId, content);
        data.put(message.getId(), message);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        if (id == null) {
            return null;
        }

        return data.get(id);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        if (channelId == null) {
            return null;
        }

        return data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findAllByAuthorId(UUID authorId) {
        if (authorId == null) {
            return null;
        }

        return data.values().stream()
                .filter(message -> message.getAuthorId().equals(authorId))
                .toList();
    }

    @Override
    public List<Message> findAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(UUID id, String content) {
        if (id == null) {
            return null;
        }

        Message message = data.get(id);

        if (message == null) {
            return null;
        }

        message.update(content);

        return message;
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            return;
        }

        data.remove(id);
    }
}
