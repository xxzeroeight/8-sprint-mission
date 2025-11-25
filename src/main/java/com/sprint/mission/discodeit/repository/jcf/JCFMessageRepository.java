package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository
{
    private final Map<UUID, Message> data = new HashMap<>();

    private static JCFMessageRepository instance;

    private JCFMessageRepository() {}

    public static JCFMessageRepository getInstance() {
        if (instance == null) {
            instance = new JCFMessageRepository();
        }

        return instance;
    }
    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
