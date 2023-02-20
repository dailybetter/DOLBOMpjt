package com.c103.dolbom.openvidu.repository;

import com.c103.dolbom.Entity.MemberConference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberConferenceRepository extends JpaRepository<MemberConference,Long> {

}
