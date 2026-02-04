package com.sprint.mission.discodeit.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode
{
    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    BINARY_CONTENT_NOT_FOUND("바이너리 컨텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    READSTATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USERSTATUS_NOT_FOUND("사용자 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    DUPLICATE_USER("이미 존재하는 사용자입니다.", HttpStatus.CONFLICT),
    DUPLICATE_CHANNEL("이미 존재하는 채널입니다.", HttpStatus.CONFLICT),
    DUPLICATE_READSTATUS("이미 해당 채널의 읽음 상태가 존재합니다.", HttpStatus.CONFLICT),

    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다.", HttpStatus.FORBIDDEN);

    private final int status;
    private final String message;

    ErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
