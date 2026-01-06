package com.sprint.mission.discodeit.common.exception;

import com.sprint.mission.discodeit.auth.exception.InvalidPasswordException;
import com.sprint.mission.discodeit.channel.exception.ChannelUpdateNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        log.warn("리소스 없음: [{}]", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateException(DuplicateException e) {
        log.warn("중복 데이터: [{}].", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(ChannelUpdateNotAllowedException.class)
    public ResponseEntity<String> handleChannelUpdateNotAllowedException(ChannelUpdateNotAllowedException e) {
        log.warn("권한 없음: [{}]", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException e) {
        log.warn("인증 실패: [{}]", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        log.warn("예기치 못한 에러: [{}]", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }
}
