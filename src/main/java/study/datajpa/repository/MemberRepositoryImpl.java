package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    // 커스텀 리포지토리를 사용할때 규칙이 하나있음. 스프링 데이터 jpa를 사용하는 MemberRepository 가 있잖아. 확장해서 커스텀해서 사용하는경우에는 MemberRepository 뒤에 Impl 을 붙여줘야함

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();

    }
}
