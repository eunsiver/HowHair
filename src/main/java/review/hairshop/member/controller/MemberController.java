package review.hairshop.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import review.hairshop.member.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
}
