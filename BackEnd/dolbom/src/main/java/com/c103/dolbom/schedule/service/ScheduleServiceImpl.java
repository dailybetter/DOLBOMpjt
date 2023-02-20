package com.c103.dolbom.schedule.service;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.Entity.Schedule;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.repository.MemberRepository;
import com.c103.dolbom.schedule.dto.GetSchedule;
import com.c103.dolbom.schedule.dto.ScheduleDto;
import com.c103.dolbom.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final MemberClientRepository memberClientRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ScheduleDto.Detail> getScheduleList(Member member) {

        Map<Long, List<ScheduleDto.Detail>> scheduleMap = new HashMap<>();
        List<ScheduleDto.Detail> memberScheduleList = new ArrayList<>();
        List<GetSchedule> scheduleList = scheduleRepository.getScheduleList(member.getId());

        StringBuilder sbStart = new StringBuilder();
        StringBuilder sbEnd = new StringBuilder();

        for(GetSchedule gs : scheduleList) {
            if(!scheduleMap.containsKey(gs.getClientId()))
                scheduleMap.put(gs.getClientId(), new ArrayList<ScheduleDto.Detail>());

            String[] startTimeArr = gs.getStartTime().split(" ");
            String[] endTimeArr = gs.getEndTime().split(" ");

            scheduleMap.get(gs.getClientId()).add(ScheduleDto.Detail.builder()
                            .scheduleId(gs.getScheduleId())
                            .counselorId(gs.getCounselorId())
                            .clientId(gs.getClientId())
                            .start(sbStart.append(startTimeArr[0]).append("T").append(startTimeArr[1]).append("Z").toString())
                            .end(sbEnd.append(endTimeArr[0]).append("T").append(endTimeArr[1]).append("Z").toString())
                            .counselorName(member.getName())
                            .title("임시 이름")
                            .content(gs.getContent())
                    .build());

            sbStart.setLength(0);
            sbEnd.setLength(0);
        }

        // client id -> client name 조회
        List<Long> clientIdList = new ArrayList<>(scheduleMap.keySet());
        List<Member> byIdIn = memberRepository.findByIdIn(clientIdList);
        Map<Long, String> idToNameMap = new HashMap<>();
        for(Member m : byIdIn) {
            idToNameMap.put(m.getId(), m.getName());
        }

        for(Long clientId : scheduleMap.keySet()) {
            String clientName = idToNameMap.get(clientId);
            for(ScheduleDto.Detail sd :scheduleMap.get(clientId)) {
                sd.setTitle(clientName);
                memberScheduleList.add(sd);
            }
        }

        return memberScheduleList;
    }

    @Override
    public ScheduleDto.Detail getScheduleDetail(long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        MemberClient mc = memberClientRepository.findById(schedule.getMemberClientId().getId())
                .orElseThrow(() -> new IllegalArgumentException("relation doesn't exist"));

        // 나중에 Member 테이블로 들어가는 쿼리 2방(내담자, 상담자)을 1방으로 줄일 수도 있을듯
        ScheduleDto.Detail detailScheduleDto = ScheduleDto.Detail.builder()
                .scheduleId(schedule.getId())
                .counselorId(mc.getMember().getId())
                .clientId(mc.getClient().getId())
                .start(setLocalDateTimeToISO(schedule.getStartTime()))
                .end(setLocalDateTimeToISO(schedule.getEndTime()))
                .content(schedule.getContent())
                .counselorName(mc.getMember().getName())
                .title(mc.getClient().getName())
                .build();
        return detailScheduleDto;
    }

    @Override
    public long createSchedule(ScheduleDto.Detail scheduleDto) {

        Optional<MemberClient> mcOpt = memberClientRepository.findByMemberIdAndClientId(scheduleDto.getCounselorId(), scheduleDto.getClientId());

        MemberClient memberClient = null;
        if(mcOpt.isEmpty()) {
            Member counselor = memberRepository.findById(scheduleDto.getCounselorId())
                    .orElseThrow(()-> new IllegalArgumentException("counselor doesn't exist"));

            Member client = memberRepository.findById(scheduleDto.getClientId())
                    .orElseThrow(()-> new IllegalArgumentException("client doesn't exist"));

            MemberClient mc = MemberClient.builder()
                    .client(client)
                    .member(counselor)
                    .build();

            memberClient = memberClientRepository.save(mc);
        } else {
            memberClient = mcOpt.get();
        }

        LocalDateTime startTime = setISOToLocalDateTime(scheduleDto.getStart());
        LocalDateTime endTime = setISOToLocalDateTime(scheduleDto.getEnd());

        Schedule schedule = Schedule.builder()
                .memberClientId(memberClient)
                .startTime(startTime)
                .endTime(endTime)
                .content(scheduleDto.getContent())
                .build();

        schedule = scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Override
    public long updateSchedule(ScheduleDto.Basic scheduleDto) {

        Schedule schedule = scheduleRepository.findById(scheduleDto.getScheduleId())
                .orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.updateSchedule(
                setISOToLocalDateTime(scheduleDto.getStart()), setISOToLocalDateTime(scheduleDto.getEnd()), scheduleDto.getContent());

        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Override
    public long deleteSchedule(long scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));
        scheduleRepository.delete(schedule);

        return scheduleId;
    }

    @Override
    public LocalDateTime setISOToLocalDateTime(String ISODateTime) {

        // 파씽 오류시 DateTimeParseException 예외 발생
        // 추후에 ControllerAdvice로 예외처리 필요

        // 한국 시간으로 파싱하는 경우
//        LocalDateTime dateTime = LocalDateTime.from(
//                Instant.from(
//                        DateTimeFormatter.ISO_DATE_TIME.parse(ISODateTime)
//                ).atZone(ZoneId.of("Asia/Seoul"))
//        );

        Instant instant = Instant.parse(ISODateTime);

        //Convert instant to LocalDateTime, no timezone, add a zero offset / UTC+0
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);

        return dateTime;
    }

    @Override
    public String setLocalDateTimeToISO(LocalDateTime localDateTime) {

//        String ISOTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        String ISOTime = instant.toString();
        return ISOTime;
    }

}
