package study.datajpa.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}") // 권장하는 방식 아님 // 데이터 조회용으로만 사용하는게 좋음
    public String findMember2(@PathVariable("id") Member member) { // 스프링이 Member에서 ID찾고 하는 컨버팅을 모두 해줘서 결과를 바로 인젝션 해줌
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) { // page, size , sort, desc

        // http://localhost:8080/members?page=1&size=3 , default값 ( pageSize 같은 ) 걸 바꾸고싶으면
        // 글로벌 설정  - application.yml spring.data.web.pageable.default-page-size
        // 개별 설정  - @PageableDefault(size = 10, sort = "username") Pageable Pageable
        System.out.println("pageable.getOffset() = " + pageable.getOffset());
        System.out.println("pageable.getPageNumber() = " + pageable.getPageNumber());
        System.out.println("pageable.getSort() = " + pageable.getSort());
        System.out.println("pageable.getPageSize() = " + pageable.getPageSize());
        Page<Member> page = memberRepository.findAll(pageable);

        // ++ 페이징 정보가 둘 이상이면 접두사로 구분
        // @Qualifier("member) Pageable memberPageable, @Qualifier("order") Pageable orderPageable, ...
        // ex ) /members?member_page=0&order_page=1


        return page;
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }



}
