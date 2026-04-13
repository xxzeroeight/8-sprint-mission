package com.sprint.mission.discodeit.auth.service;

import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.auth.dto.info.JwtInformation;
import com.sprint.mission.discodeit.domain.user.mapper.UserMapper;
import com.sprint.mission.discodeit.domain.user.repository.UserRepository;
import com.sprint.mission.discodeit.global.exception.InvalidTokenException;
import com.sprint.mission.discodeit.global.exception.TokenGenerationException;
import com.sprint.mission.discodeit.global.secutiry.JwtRegistry;
import com.sprint.mission.discodeit.global.secutiry.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final JwtRegistry jwtRegistry;

    @Override
    public JwtInformation refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken) || !jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!(userDetails instanceof DiscodeitUserDetails discodeitUserDetails)) {
            throw new InvalidTokenException();
        }

        try {
            String newAccessToken = jwtTokenProvider.generateAccessToken(discodeitUserDetails);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(discodeitUserDetails);

            JwtInformation jwtInformation = new JwtInformation(
                    discodeitUserDetails.getUserDto(),
                    newAccessToken,
                    newRefreshToken
            );

            jwtRegistry.rotateJwtInformation(
                    refreshToken,
                    jwtInformation
            );

            return jwtInformation;

        } catch (JOSEException e) {
            throw new TokenGenerationException();
        }
    }
}
