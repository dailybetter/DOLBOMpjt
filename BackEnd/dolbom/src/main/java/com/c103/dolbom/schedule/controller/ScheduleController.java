package com.c103.dolbom.schedule.controller;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.schedule.dto.ScheduleDto;
import com.c103.dolbom.schedule.service.ScheduleService;
import com.c103.dolbom.user.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<?> getScheduleDetail(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();
        List<ScheduleDto.Detail> scheduleListByPeriod = scheduleService.getScheduleList(member);
        return new ResponseEntity<List<ScheduleDto.Detail>>(scheduleListByPeriod, HttpStatus.OK);

    }


    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleDetail(@PathVariable long scheduleId) {

        ScheduleDto.Detail scheduleDto = scheduleService.getScheduleDetail(scheduleId);
        return ResponseEntity.ok(scheduleDto);
    }

    // 스케줄 등록
    @PostMapping
    public ResponseEntity<?> createSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ScheduleDto.Detail scheduleDto) {

        scheduleDto.setCounselorId(principalDetails.getMember().getId());
        long scheduleId = scheduleService.createSchedule(scheduleDto);

        return ResponseEntity.ok(new ScheduleDto.Id(scheduleId));
    }

    @PutMapping
    public ResponseEntity<?> updateSchedule(@RequestBody ScheduleDto.Basic scheduleDto) {

        long scheduleId = scheduleService.updateSchedule(scheduleDto);

        return ResponseEntity.ok(new ScheduleDto.Id(scheduleId));
    }

    // 추후에 로그인 기능과 합치면 spring security 에서 받아온 유저가 주인이 맞는지 확인하고 삭제하기
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable long scheduleId) {

        scheduleId = scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.ok(new ScheduleDto.Id(scheduleId));
    }
}
