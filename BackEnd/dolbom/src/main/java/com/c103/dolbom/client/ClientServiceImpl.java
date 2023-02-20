package com.c103.dolbom.client;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.Entity.Role;
import com.c103.dolbom.client.dto.ClientJoinDto;
import com.c103.dolbom.client.dto.ClientDto;
import com.c103.dolbom.client.dto.ClientSimpleDto;
import com.c103.dolbom.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService{
    private final MemberClientRepository memberClientRepository;
    private final MemberRepository memberRepository;
    @Override
    public List<ClientSimpleDto> getClientListByMemberId(Long memberId) {
        log.info("getClientListByMemberId " + memberId);
        List<MemberClient> list = memberClientRepository.findByMemberId(memberId);

        List<ClientSimpleDto> clientList = new ArrayList<>();

        for(MemberClient mc : list){
            ClientSimpleDto dto = ClientSimpleDto.builder()
                    .id(mc.getClient().getId())
                    .name(mc.getClient().getName())
                    .phone(mc.getClient().getPhone())
                    .email(mc.getClient().getEmail())
                    .build();
            clientList.add(dto);
        }
        return clientList;
    }

    @Override
    public List<ClientSimpleDto> getClientListByName(String name) {
        List<Member> clientList = memberRepository.findByName(name);


        List<ClientSimpleDto> clientDtoList = new ArrayList<>();

        for(Member member : clientList){
            ClientSimpleDto dto = ClientSimpleDto.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .phone(member.getPhone())
                    .email(member.getEmail())
                    .build();
            clientDtoList.add(dto);
        }
        return clientDtoList;
    }

    @Override
    public ClientDto getClient(Long clientId) {
        Member clientEntity = memberRepository.findById(clientId).get();
        ClientDto dto = ClientDto.builder()
                .id(clientId)
                .content(clientEntity.getContent())
                .birth(clientEntity.getBirth())
                .email(clientEntity.getEmail())
                .name(clientEntity.getName())
                .phone(clientEntity.getPhone())
                .build();


        return dto;
    }

    @Override
    public Long joinClient(ClientJoinDto dto,Long counselorId) {
        //같은 이메일이 존재하다면 예외 발생

        Member entityMember = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .role(Role.CLIENT)
                .phone(dto.getPhone())
                .password(randomString())
                .birth(dto.getBirth())
                .build();

        Member client = memberRepository.save(entityMember);
        Member member = memberRepository.findById(counselorId).get();

        MemberClient memberClient = MemberClient.builder()
                .member(member)
                .client(client)
                .build();

        Long memberClientId = memberClientRepository.save(memberClient).getId();
        return memberClientId;
    }

    @Override
    public Long joinRegisteredClient(Long client_id, Long member_id) {
        Member client = memberRepository.findById(client_id).get();
        Member member = memberRepository.findById(member_id).get();

        MemberClient memberClient = MemberClient.builder()
                .member(member)
                .client(client)
                .build();

        Long memberClientId = memberClientRepository.save(memberClient).getId();

        return memberClientId;
    }

    @Override
    public Long modifyClient(ClientDto dto) {
        Member client = memberRepository.findById(dto.getId()).get();

        client.changeName(dto.getName());
        client.changeBirth(dto.getBirth());
        client.changeContent(dto.getContent());
        client.changePhone(dto.getPhone());

        Long clientId = memberRepository.save(client).getId();

        return clientId;
    }

    @Override
    @Transactional
    public int deleteClient(Long clientId,Long memberId) {
        memberClientRepository.deleteByClientIdAndMemberId(clientId, memberId);
        return 1;
    }

    @Override
    public Long getClientMemberId(Long client_id, Long member_id) {
        Optional<MemberClient> memberClient = memberClientRepository.findByMemberIdAndClientId(member_id,client_id);
        return memberClient.get().getId();
    }

    private String randomString(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 45;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }


}
