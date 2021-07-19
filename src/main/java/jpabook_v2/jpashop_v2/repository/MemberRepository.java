package jpabook_v2.jpashop_v2.repository;

import jpabook_v2.jpashop_v2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    //@PersistenceContext//스프링이 JPA의 EntityManager 생성해서 자동 주입.
    private final EntityManager em;

    public void save(Member member){
        //영속성 컨텍스트에 Member 객체 넣는다.
        //트랜잭션이 커밋되는 시점에 DB에 반영.(DB에 insert 쿼리가 날아감)
        em.persist(member);
    }

    public Member findOne(Long id){
        //JPA의 find메서드 사용
        //find(타입, PK)
        return em.find(Member.class,id);
    }
    public List<Member> findAll(){
        //SQL과 거의 똑같은데 from의 대상이 테이블이 아니라 엔티티!
        //JPQL : 테이블이 아니라 엔티티 대상으로 쿼리한다! ,Member.class:조회 타입!
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    // m.name = :name 파라미터 바인딩해서 특정 이 회원들만 조회
    public List<Member> findByName(String name){//,Member.class : 조회 타입은 Member
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
