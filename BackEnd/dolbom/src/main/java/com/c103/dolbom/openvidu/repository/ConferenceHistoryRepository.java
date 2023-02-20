package com.c103.dolbom.openvidu.repository;

import com.c103.dolbom.Entity.Conference;
import com.c103.dolbom.Entity.ConferenceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceHistoryRepository extends JpaRepository<ConferenceHistory,Long> {
    List<ConferenceHistory> findAllByConferenceId(Long conferenceId);
    List<ConferenceHistory> findAllByConference(Conference conference);
}
