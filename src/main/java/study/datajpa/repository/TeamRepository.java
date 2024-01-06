package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Team;

//@Repository 생략 가능 , JpaRepository 를 상속 받은걸 보고 프록시 객체를 넣어줘야겠구나 해서 넣어줌
// JpaRepository 를 상속받으면 스프링 데이터 JPA 가 어플리케이션 실행 시점에 구현 클래스를 대신 생성해서 주입해줌
public interface TeamRepository extends JpaRepository<Team, Long> {

}
