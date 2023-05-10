package com.apps.springbootaws.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    /*
        Spring Security 에서는 권한 코드에 항상 ROLE_ 이 앞에 붙어야 함.
     */
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;
}
