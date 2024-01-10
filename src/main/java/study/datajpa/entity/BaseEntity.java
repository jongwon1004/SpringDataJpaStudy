package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    // 위의 두 데이터는 날짜값이 알아서 들어가겠지만, 밑의 두 데이터는 값을 어떻게 넣어야해 ?
    // 밑의 두 데이터가 수정되거나 추가될때마다 DataJapApplication 에 설정한 Bean auditorProvider 메서드를 호출해서 자동으로 값을 가져옴

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
