package nextstep.member.ui;

import nextstep.auth.authorization.AuthenticationPrincipal;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.application.dto.MemberResponse;
import nextstep.member.domain.LoginMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

@RestController
public class MemberController {
    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberService.findMember(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // 로그인 인증 프로세스 실습 - session 체크용
//    @GetMapping("/members/me")
//    public ResponseEntity<MemberResponse> findMemberOfMine(HttpServletRequest request) {
//        SecurityContext context = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
//        LoginMember loginMember = (LoginMember) context.getAuthentication().getPrincipal();
//        MemberResponse memberResponse = memberService.findMember(loginMember.getId());
//        return ResponseEntity.ok().body(memberResponse);
//    }
    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = memberService.findMember(loginMember.getId());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody MemberRequest param) {
        memberService.updateMember(loginMember.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        memberService.deleteMember(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
