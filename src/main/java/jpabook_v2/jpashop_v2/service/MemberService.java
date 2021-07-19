package jpabook_v2.jpashop_v2.service;

import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //1.회원 가입
    @Transactional
    public Long join(Member member){
        //중복회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION : 최적화하자면 갯수만 카운팅해서 0보다 크면 으로 해줘도 된다!
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
    //2. 회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //3. 회원 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

    @Transactional//변경감지
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);//영속성 컨텍스트에서 먼저 찾고, 없으면 DB에서 가져온다.
        member.setName(name);//영속상태의 member의 name을 바꿔주면 스피링 AOP가 동작하면서 @Transactional 애노테이션에 의해 트랜잭션 관련 AOP 끝나는 시점에 트랜잭션이 커밋된다.
        //그 때 JPA가 DB에 flush하면서 영속성 컨텍스트의 것들을 커밋한다!
    }
}
