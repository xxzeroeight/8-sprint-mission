package com.sprint.mission.discodeit.global.secutiry;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.auth.service.DiscodeitUserDetails;
import com.sprint.mission.discodeit.domain.user.dto.domain.UserDto;
import com.sprint.mission.discodeit.global.exception.InvalidTokenException;
import com.sprint.mission.discodeit.global.exception.MissingTokenClaimException;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider
{
    public static final String REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN";

    private final JWSSigner accessTokenSigner;
    private final JWSVerifier accessTokenVerifier;
    private final JWSSigner refreshTokenSigner;
    private final JWSVerifier refreshTokenVerifier;

    private final int accessTokenExpirationMs;
    private final int refreshTokenExpirationMs;

    public JwtTokenProvider(@Value("${jwt.access-token.secret}") String accessTokenSecret,
                            @Value("${jwt.refresh-token.secret}") String refreshTokenSecret,
                            @Value("${jwt.access-token.expiration}") int accessTokenExpirationMs,
                            @Value("${jwt.refresh-token.expiration}") int refreshTokenExpirationMs) throws JOSEException
    {
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;

        byte[] accessSecretBytes = accessTokenSecret.getBytes(StandardCharsets.UTF_8);
        this.accessTokenSigner = new MACSigner(accessSecretBytes);
        this.accessTokenVerifier = new MACVerifier(accessSecretBytes);

        byte[] refreshSecretBytes = refreshTokenSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshTokenSigner = new MACSigner(refreshSecretBytes);
        this.refreshTokenVerifier = new MACVerifier(refreshSecretBytes);
    }

    public String generateAccessToken(DiscodeitUserDetails userDetails) throws JOSEException {
        return generateToken(userDetails, accessTokenExpirationMs, accessTokenSigner, "access");
    }

    public String generateRefreshToken(DiscodeitUserDetails userDetails) throws JOSEException {
        return generateToken(userDetails, refreshTokenExpirationMs, refreshTokenSigner, "refresh");
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessTokenVerifier, "access");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshTokenVerifier, "refresh");
    }

    public String getUsernameFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public String getTokenId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getJWTID();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public UUID getUserId(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String userIdStr = (String) signedJWT.getJWTClaimsSet().getClaim("userId");
            if (userIdStr == null) {
                throw new MissingTokenClaimException();
            }
            return UUID.fromString(userIdStr);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Cookie generateRefreshTokenCookie(String refreshToken) {
        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(refreshTokenExpirationMs / 1000);
        return refreshCookie;
    }

    public Cookie generateRefreshTokenExpirationCookie() {
        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0);
        return refreshCookie;
    }

    private String generateToken(DiscodeitUserDetails userDetails, int expirationMs, JWSSigner signer, String tokenType) throws JOSEException
    {
        UserDto user = userDetails.getUserDto();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.username())
                .jwtID(UUID.randomUUID().toString())
                .claim("userId", user.id().toString())
                .claim("type", tokenType)
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issueTime(now)
                .expirationTime(expiryDate)
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        signedJWT.sign(signer);
        String token = signedJWT.serialize();

        return token;
    }

    private boolean validateToken(String token, JWSVerifier verifier, String expectedType) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(verifier)) {
                return false;
            }

            String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("type");
            if (!expectedType.equals(tokenType)) {
                return false;
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            return expirationTime != null && !expirationTime.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
