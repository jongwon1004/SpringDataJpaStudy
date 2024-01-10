package study.datajpa.entity;



import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

/*
    create table Member (
        age integer not null,
        createdDate datetime(6), ***
        member_id bigint not null auto_increment,
        team_id bigint,
        updatedDate datetime(6), ***
        username varchar(255),
        primary key (member_id)
    ) engine=InnoDB
 */

@Getter
@MappedSuperclass // 얘는 진짜 상속관계가 아니고 속성들(createdDate, updatedDate) 밑에(자식들) 한테 내려서 테이블에서 같이 쓸수있게하는, 데이터만 공유하게 하는 애임. JPA 상속관계 X
public class JpaBaseEntity {

    @Column(updatable = false) // 혹시라도 값이 바뀌어도 DB에 업데이트 쿼리 안나감
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist 하기 전에 이벤트가 발생하는것 ( 저장 하기 전에 이 이벤트가 발생 )
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
