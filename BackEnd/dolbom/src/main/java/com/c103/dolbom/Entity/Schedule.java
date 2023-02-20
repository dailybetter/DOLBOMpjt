package com.c103.dolbom.Entity;

import com.c103.dolbom.schedule.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name="schedule")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule extends BaseTimeEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_client_id")
    private MemberClient memberClientId;

    @Column(name="start_time")
    private LocalDateTime startTime;
    @Column(name="end_time")
    LocalDateTime endTime;
    @Column(columnDefinition = "text")
    private String content;

    public void updateSchedule(
            LocalDateTime startTime, LocalDateTime endTime, String content) {

        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
    }
}
