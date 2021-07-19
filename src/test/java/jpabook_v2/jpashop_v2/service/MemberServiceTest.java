//package jpabook_v2.jpashop_v2.service;
//
//import jpabook_v2.jpashop_v2.domain.Member;
//import jpabook_v2.jpashop_v2.repository.MemberRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import static org.junit.Assert.*;
//
////단순 단위 테스트가 아니라 스프링 부트와 통합해서 메모리에 DB 올라가는 것까지 테스트하기 위해
//@RunWith(SpringRunner.class)//테스트할 때 스프링과 함께 실행할 때.
//@SpringBootTest//스프링부트를 띄운 상태로(스프링 컨테이너 안애서) 테스트하겠다. 없으면 @Autowired 다 실패한다.
//@Transactional//테스트 시에는 롤백
//public class MemberServiceTest {
//    @Autowired MemberService memberService;
//    @Autowired MemberRepository memberRepository;
//    @Autowired EntityManager em;
//
//    @Test
//    //@Rollback(false)
//    public void 회원가입() throws Exception{
//        //given
//        Member member = new Member();
//        member.setName("Jo");
//        //when
//        Long savedId = memberService.join(member);
//
//        //then 제대로 저장됐는지 검증 : 생성한 member 객체 인스턴스와 저장한 member의 id 똑같아야한다
//        //em.flush();
//        assertEquals(member,memberRepository.findOne(savedId));
//    }
//    @Test(expected = IllegalStateException.class)
//    public void 중복_회원_예외() throws Exception{
//        //given
//        Member member1 = new Member();
//        member1.setName("Jo");
//
//        Member member2 = new Member();
//        member2.setName("Jo");
//
//        //when : 동일한 이름을 넣었을 때
//        memberService.join(member1);
//        memberService.join(member2);//여기서 예외 터져서 나간 에러가 IllegalStateException이면 테스트 성공!
//
//        //then : 예외가 발생해야 한다!!!
//        fail("예외가 발생해야 한다");
//
//    }
//}