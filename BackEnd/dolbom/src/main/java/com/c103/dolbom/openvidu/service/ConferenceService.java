package com.c103.dolbom.openvidu.service;

import com.c103.dolbom.openvidu.dto.JoinSessionDto;
import com.c103.dolbom.openvidu.dto.MemoDto;
import com.c103.dolbom.openvidu.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface ConferenceService {

    Long createConference(Long memberId, String sessionId);
    Long createMemberConference(JoinSessionDto dto);
    int saveMemo(MemoDto dto);

}
