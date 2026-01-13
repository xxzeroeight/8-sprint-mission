package com.sprint.mission.discodeit.domain.user.domain;

import com.sprint.mission.discodeit.domain.BaseUpdatableEntity;
import com.sprint.mission.discodeit.domain.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.userstatus.domain.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
@Entity
public class User extends BaseUpdatableEntity
{
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    // 생명주기가 같음. (revoke는 삭제만.)
    // q. 고의로 끊을 일이 있나? orphanRemoval는 방어적? 명시적?
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserStatus userStatus;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private BinaryContent profile;

    public User(String username, String password, String email, BinaryContent profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.userStatus = new UserStatus(this);
    }

    public void update(String username, String password, String email, BinaryContent profile) {
        if (username != null) {
            this.username = username;
        }
        if (email != null) {
            this.email = email;
        }
        if (password != null) {
            this.password = password;
        }
        if (profile != null) {
            this.profile = profile;
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
