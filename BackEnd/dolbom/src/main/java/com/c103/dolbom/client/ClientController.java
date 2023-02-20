package com.c103.dolbom.client;

import com.c103.dolbom.client.dto.ClientCounselorDto;
import com.c103.dolbom.client.dto.ClientJoinDto;
import com.c103.dolbom.client.dto.ClientDto;
import com.c103.dolbom.client.dto.ClientSimpleDto;
import com.c103.dolbom.user.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController { //프로필 이미지 미완, 권한 ADMIN,COUNSELOR
    private final ClientService clientService;
    //상담자의 등록된 내담자 전체 정보 불러오기
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getClientListByMember(@PathVariable("id") Long memberId){
//        List<ClientSimpleDto> clientList = clientService.getClientListByMemberId(memberId);
//        return new ResponseEntity<>(clientList, HttpStatus.OK);
//    }
    @GetMapping("")
    public ResponseEntity<?> getClientListByMember(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<ClientSimpleDto> clientList = clientService.getClientListByMemberId(principalDetails.getMember().getId());
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }
    //내담자 상세정보 + 맴버클라이언트아이디도 같이 리턴해줘야함
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getClient(@PathVariable("id") Long clientId){
        ClientDto dto = clientService.getClient(clientId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    //기존에 없는 내담자 등록
    @PostMapping
    public ResponseEntity<?> clientJoin(@RequestBody ClientJoinDto dto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberClientId = clientService.joinClient(dto,principalDetails.getMember().getId());
        return new ResponseEntity<>(memberClientId, HttpStatus.OK);
    }
    //내담자 수정
    @PatchMapping
    public ResponseEntity<?> clientModify(@RequestBody ClientDto dto){
        Long clientId = clientService.modifyClient(dto);
        return new ResponseEntity<>(clientId, HttpStatus.OK);
    }

    //상담사의 등록된 내담자 삭제
//    @DeleteMapping("/{client_id}/{counselor_id}")
//    public ResponseEntity<?> clientDelete(@PathVariable("client_id") Long clientId,@PathVariable("counselor_id") Long counselorId){
//        int msg = clientService.deleteClient(clientId,counselorId);
//        return new ResponseEntity<>("삭제완료", HttpStatus.OK);
//    }
    @DeleteMapping("/{client_id}")
    public ResponseEntity<?> clientDelete(@PathVariable("client_id") Long clientId,@AuthenticationPrincipal PrincipalDetails principalDetails){
        int msg = clientService.deleteClient(clientId,principalDetails.getMember().getId());
        return new ResponseEntity<>("삭제완료", HttpStatus.OK);
    }
    //등록 전 이름으로 내담자 검색
    @GetMapping("/name/{name}")
    public ResponseEntity<?> memberGetByName(@PathVariable("name") String name){
        List<ClientSimpleDto> clientList = clientService.getClientListByName(name);
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }
    //이미 있는 내담자 등록
//    @PostMapping("/registered")
//    public ResponseEntity<?> registeredClientJoin(@RequestBody ClientCounselorDto dto){
//        Long memberClientId = clientService.joinRegisteredClient(dto.getClientId(),dto.getCounselorId());
//        return new ResponseEntity<>(memberClientId, HttpStatus.OK);
//    }
    @PostMapping("/registered")
    public ResponseEntity<?> registeredClientJoin(@RequestBody Long id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberClientId = clientService.joinRegisteredClient(id, principalDetails.getMember().getId());
        return new ResponseEntity<>(memberClientId, HttpStatus.OK);
    }
//    @PostMapping("/registered")
//    public ResponseEntity<?> registeredClientJoin(@RequestBody ClientCounselorDto dto){
//        Long memberClientId = clientService.joinRegisteredClient(dto.getClientId(),dto.getCounselorId());
//        return new ResponseEntity<>(memberClientId, HttpStatus.OK);
//    }
    //상담사 내담자 관계 아이디 조회
    @GetMapping("/{client_id}")
    public ResponseEntity<?> getClientCounselorId(@PathVariable("client_id") Long clientId,@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long id = clientService.getClientMemberId(clientId,principalDetails.getMember().getId());
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
//    @GetMapping("/{client_id}/{counselor_id}")
//    public ResponseEntity<?> getClientCounselorId(@PathVariable("client_id") Long clientId,@PathVariable("counselor_id") Long counselorId){
//        Long id = clientService.getClientMemberId(clientId,counselorId);
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }

}
