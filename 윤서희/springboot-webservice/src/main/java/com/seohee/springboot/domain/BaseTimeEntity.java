package com.seohee.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 필드들도 칼럼으로 인식
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스에 Auditing 기능 포함
public class BaseTimeEntity { // 모든 Entity 의 상위클래스가 되어 createdDate, modifiedDate 를 자동으로 관리

    @CreatedDate // Entity 시간 자동 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // Entity 의 값을 변경할 때 시간 자동 저장
    private LocalDateTime modifiedDate;
}
