package com.c103.dolbom.schedule.dto;

import java.time.LocalDateTime;

public interface GetSchedule {

    Long getScheduleId();
    Long getCounselorId();
    Long getClientId();

    String getStartTime();
    String getEndTime();
    String getContent();
}
