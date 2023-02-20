package com.c103.dolbom.client;


import com.c103.dolbom.client.dto.ClientJoinDto;
import com.c103.dolbom.client.dto.ClientDto;
import com.c103.dolbom.client.dto.ClientSimpleDto;

import java.util.List;

public interface ClientService {
    List<ClientSimpleDto> getClientListByMemberId(Long memberId);
    List<ClientSimpleDto> getClientListByName(String name);
    ClientDto getClient(Long clientId);
    Long joinClient(ClientJoinDto dto, Long counselorId);
    Long joinRegisteredClient(Long client_id, Long member_id);

    Long modifyClient(ClientDto dto);
    int deleteClient(Long clientId,Long memberId);
    Long getClientMemberId(Long client_id, Long member_id);
}
