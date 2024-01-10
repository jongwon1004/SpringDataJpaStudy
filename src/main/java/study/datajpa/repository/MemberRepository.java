package study.datajpa.repository;


import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {


    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3By();

    //    @Query(name = "Member.findByUsername") // 네임드 쿼리를 먼저 찾고 만약에 네임드쿼리가 없으면 메서드 이름으로 쿼리 생성
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findByUsernameList();

    // Dto 로 반환
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalMemberByUsername(String username);

    //        @Query("select m from Member m left join m.team t") // 이렇게만 하면 team 도 count 쿼리가 나가니까
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    // 이렇게 적어주면 카운트 쿼리를 딱 정해줄 수 있음 . (count 쿼리 분리)
    // 복잡한 쿼리에서 사용, 데이터는 left join , 카운트 쿼리는 left join 안해도 됨
    // PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")); 여기서 Sorting 조건이 복잡해지면 그냥 jpql에 Sorting 조건 적어줘도됨.
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    // 이걸 붙혀주지 않으면 getResultList 나 getSingleResult 같은 쿼리가 나가버림. executeUpdate() 같은거라 생각하면 됨
    // clearAutomatically = true 는 이 쿼리가 나가고 em.clear 역할을 자동으로 해줌
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query(value = "select m from Member m left join fetch m.team t")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})  // EntityGraph + JPQL
    @Query("select m from Member m")
        // 쿼리는 짰는데 페치 조인만 추가하고 싶을때 이렇게 사용해도됨
    List<Member> findMemberEntityGraph();

    //    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    // Member 엔티티의 @NamedEntityGraph 를 사용한것
    List<Member> findMembersByUsername(@Param("username") String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username); // DB for update 찾아볼것

}
