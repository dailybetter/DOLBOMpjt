package com.c103.dolbom.user.controller;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.user.auth.PrincipalDetails;
import com.c103.dolbom.user.dto.MemberDto;
import com.c103.dolbom.user.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberServiceImpl memberService;

    @GetMapping("/mypage")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();
        MemberDto.Basic ud = MemberDto.Basic.builder()
                .id(member.getId())
                .role(member.getRole().toString())
                .email(member.getEmail())
                .name(member.getName())
                .content(member.getContent())
                .birth(member.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .phone(member.getPhone())
                .build();

        return new ResponseEntity<MemberDto.Basic> (ud, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> checkIdDuplicated(@PathVariable String email) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("isExist", memberService.checkIdDuplicated(email));
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MemberDto.Basic memberDto) {
        Long memberId = memberService.createMember(memberDto);

        return new ResponseEntity<Long>(memberId, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody MemberDto.Basic memberDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member authUser = principalDetails.getMember();

        Long userMemberId = memberService.updateUser(authUser, memberDto);

        return new ResponseEntity<Long>(userMemberId, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member authUser = principalDetails.getMember();

        boolean check = memberService.deleteUser(authUser);
        return new ResponseEntity<String>("delete OK", HttpStatus.OK);

    }
}
