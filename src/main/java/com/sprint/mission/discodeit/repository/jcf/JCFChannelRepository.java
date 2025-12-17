package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf",
        matchIfMissing = true
)
@Repository
public class JCFChannelRepository implements ChannelRepository
{
    private final Map<UUID, Channel> data = new HashMap<>();

    private static JCFChannelRepository instance;

    private JCFChannelRepository() {
    }
    public static JCFChannelRepository getInstance() {
        if (instance == null) {
            instance = new JCFChannelRepository();
        }

        return instance;
    }

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Channel channel = data.get(id);

        return Optional.ofNullable(channel);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
