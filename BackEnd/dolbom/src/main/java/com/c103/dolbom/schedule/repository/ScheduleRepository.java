package com.c103.dolbom.schedule.repository;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.Entity.Schedule;
import com.c103.dolbom.schedule.dto.GetSchedule;
import com.c103.dolbom.schedule.dto.ScheduleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT * FROM SCHEDULE WHERE TIMESTAMPDIFF(HOUR, :standardTime, start_time) BETWEEN 0 and :hour",nativeQuery = true)
    List<Schedule> getScheduleByTime(@Param("standardTime") String standardTime, @Param("hour") int hour);

    @Query(value = "select s.id as scheduleId, mc.member_id as counselorId, mc.client_id as clientId, s.start_time as startTime, s.end_time as endTime, s.content\n" +
            "from member_client mc join schedule s on (mc.id = s.member_client_id)\n" +
            "where mc.member_id = :id", nativeQuery = true)
    List<GetSchedule> getScheduleList(Long id);
}
