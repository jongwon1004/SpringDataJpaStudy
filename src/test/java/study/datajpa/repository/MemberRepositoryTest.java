package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 0, new Team("teamA"));

        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

//        findMember1.setUsername("newMember"); //  변경 감지

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("aaa", 3);
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findBy() {

        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);
        List<Member> top3By = memberRepository.findTop3By();
        for (Member member : top3By) {
            System.out.println("member = " + member);
        }

        assertThat(top3By.size()).isEqualTo(2);
    }

    @Test
    public void namedQuery() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsername(member1.getUsername());
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void testQuery() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findUser("aaa", 10);
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findByUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }


    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("aaa", 10);
        memberRepository.save(member1);

        member1.setTeam(team);

        List<MemberDto> result = memberRepository.findMemberDto();
        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    @Test
    public void findByNames() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<Member> result = memberRepository.findByNames(Arrays.asList("aaa", "bbb"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }


    @Test
    public void returnType() {
        Member member1 = new Member("aaa", 10);
        Member member2 = new Member("bbb", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);


        List<Member> aaa = memberRepository.findListByUsername("aaa");
        Member findMember = memberRepository.findMemberByUsername("aaa");
        System.out.println("findMember = " + findMember);

        Optional<Member> aaa1 = memberRepository.findOptionalMemberByUsername("aaa");
        Member findMember2 = aaa1.orElseGet(() -> null);
        System.out.println("findMember2 = " + findMember2);


    }

    @Test
    public void paging() {

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        memberRepository.findByAge(age, PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")));


        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest); // 여기서 바로 반환하는건 엔티티 반환하는거랑 똑같으므로 절대 안된다.

        Page<MemberDto> toMap = page.map(member ->  // map 함수를 사용해서 Dto 로 변환후 반환해 줄것
                new MemberDto(member.getId(), member.getUsername(), null)
        );


        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member : content) {
            System.out.println("member = " + member);
        }

        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(6);
        assertThat(page.getNumber()).isEqualTo(0); // 현재 몇번째 페이지인지
        assertThat(page.getTotalPages()).isEqualTo(2); // 총 몇개의 페이지가 있는지
        assertThat(page.isFirst()).isTrue(); // 지금 페이지가 첫번째 페이지인지
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 존재하는지

    }

    @Test
    public void bulkUpdate() {

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 18));
        memberRepository.save(new Member("member3", 19));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 23));
        memberRepository.save(new Member("member6", 30));

//        em.clear();

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);

        Member member6 = memberRepository.findMemberByUsername("member6");
        System.out.println("member6 = " + member6);

    }

    @Test
    public void findMemberLazy() {
        //given

        // member1 -> teamA
        // member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member memberA = new Member("memberA", 10, teamA);
        Member memberB = new Member("memberB", 10, teamB);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        em.flush();
        em.clear();

        // when
        List<Member> members = memberRepository.findMembersByUsername("memberA");
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getClass() = " + member.getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
//        List<Member> members = em.createQuery("select m from Member m join fetch m.team t", Member.class)
//                .getResultList();
//        for (Member member : members) {
//            System.out.println("memberA = " + memberA);
//        }
    }

    @Test
    public void queryHint() {

        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");
        em.flush();
    }

    @Test
    public void lock() {
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        List<Member> result = memberRepository.findLockByUsername(member1.getUsername());
    }

    @Test
    public void callCustom() {
        List<Member> findMembers = memberRepository.findMemberCustom();
        for (Member findMember : findMembers) {
            System.out.println("findMember = " + findMember);
        }
    }

    @Test
    public void specBasic() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        Specification<Member> spec = MemberSpec.username("member1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void queryByExample() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        //when
        //Probe -> 필드에 데이터가 있는 실제 도메인 객체
        Member member = new Member("member1");
        Team team = new Team("teamA");
        member.setTeam(team);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");


        Example<Member> example = Example.of(member, matcher);
        List<Member> result = memberRepository.findAll(example);
        assertThat(result.get(0).getUsername()).isEqualTo("member1");
    }

    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        List<NestedClosedProjections> memberProjection = memberRepository.findProjectionsByUsername("member1", NestedClosedProjections.class);
//        for (UsernameOnly usernameOnly : memberProjection) { // 실제 구현체는 SpringDataJpa 가 만들어서 반환해줌
//            System.out.println("usernameOnly = " + usernameOnly.getUsername());
//        }
//
//        for (UsernameOnlyDto usernameOnlyDto : memberProjection) {
//            System.out.println("usernameOnlyDto.getUsername() = " + usernameOnlyDto.getUsername());
//        }

        for (NestedClosedProjections nestedClosedProjections : memberProjection) {
            System.out.println("nestedClosedProjections = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections = " + nestedClosedProjections.getTeam().getName());
        }



    }

    @Test
    public void nativeQuery() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member1 = new Member("member1", 0, teamA);
        Member member2 = new Member("member2", 0, teamA);
        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }





}