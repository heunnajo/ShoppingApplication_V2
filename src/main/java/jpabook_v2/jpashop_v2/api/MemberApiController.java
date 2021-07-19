package jpabook_v2.jpashop_v2.api;

import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.repository.MemberRepository;
import jpabook_v2.jpashop_v2.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {//첫번째 버전의 회원등록 API

    private final MemberService memberService;

    //회원 조회 첫번째 버전 엔티티 노출
    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){//json으로 변환, 반환
        return memberService.findMembers();
    }
    //회원 조회 첫번째 버전 엔티티 노출
    @GetMapping("/api/v2/members")
    public Result membersV2(){//json으로 변환, 반환
        List<Member> findMembers = memberService.findMembers();
        //Java8 을 이용해서 Member리스트 컬렉션을 MemberDto로 변환!
        //stream을 돌린다음에 map을 쓰면 된다!map : 바꿔친다
        //결과 : List<Member> => List<MemberDto>
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        //루프를 돌려서 해도 되지만 Java8문법 이용하면 위처럼 할 수 있다!
//        for (Member findMember : findMembers) {
//            MemberDto memberDto = new MemberDto(findMember.getName());
//
//        }
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {//회원 조회시 이름만 넘기는 DTO 생성
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {//응답값을 내린다. Jsosn/xml/..로 들어온 데이터를 Member에 넣는다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {//별도의 DTO를 생성한다!
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PutMapping("/api/v2/members/{id}")//업데이트 용 DTO를 따로 만든다=> 응답 : UpdateMemberResponse  요청 : UpdateMemberRequest
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());//@Transactional로 변경 감지!
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),findMember.getName());

    }

    @Data
    private class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    private class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    private class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
