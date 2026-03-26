package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.domain.user.dto.response.UserResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails
{
    private final UserResponse userResponse;
    private final String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + userResponse.role().name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userResponse.username();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userResponse.username());
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DiscodeitUserDetails that)) {
            return false;
        }
        return Objects.equals(userResponse.username(), that.userResponse.username());
    }
}
