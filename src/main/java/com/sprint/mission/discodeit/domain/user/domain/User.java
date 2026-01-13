package com.sprint.mission.discodeit.domain.user.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import lombok.Getter;

@Getter
public class User extends BaseUpdatableEntity
{
    private String username;
    private String password;
    private String email;

    private UserStatus userStatus;
    private BinaryContent profile;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void update(String username, String password, String email) {
        if (username != null) {
            this.username = username;
        }
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
    }

    // 양방향 1:1 (무한루프 방지)
    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;

        if (userStatus != null && userStatus.getUser() != this) {
            this.userStatus.setUser(this);
        }
    }

    // 1:0 관계.
    public void setProfile(BinaryContent profile) {
        this.profile = profile;
    }
}
