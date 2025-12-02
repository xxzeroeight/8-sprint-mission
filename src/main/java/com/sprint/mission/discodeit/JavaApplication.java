package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.fixture.ChannelFixture;
import com.sprint.mission.discodeit.fixture.MessageFixture;
import com.sprint.mission.discodeit.fixture.UserFixture;
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

public class JavaApplication
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
        System.out.println("\n-----------------------In-Memory Test-----------------------");
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();

        UserFixture.userCRUDTest(userService);
        ChannelFixture.channelCRUDTest(channelService);
        MessageFixture.messageCRUDTest(messageService, userService, channelService);

        System.out.println("\n-----------------------File IO Test-----------------------");
        UserService fileUserService = FileUserService.getInstance();
        ChannelService fileChannelService = FileChannelService.getInstance();
        MessageService fileMessageService = FileMessageService.getInstance();

        UserFixture.userCRUDTest(fileUserService);
        ChannelFixture.channelCRUDTest(fileChannelService);
        MessageFixture.messageCRUDTest(fileMessageService, fileUserService, fileChannelService);

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

        /* JCF*Service or File*Service와의 차이점 */
        // "비즈니스 로직"과 "저장 로직"이 확실하게 분리되어 코드 작성이나 유지보수에 좋음.
        // 저장 방식 변경 시 Repository만 교체 (Service 레이어는 건드리지 않음.)
        // 각 클래스의 책임이 명확함.
    }
}
