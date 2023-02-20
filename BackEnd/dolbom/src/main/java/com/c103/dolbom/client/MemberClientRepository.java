package com.c103.dolbom.client;

import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.alarm.dto.AlarmMemberInterface;
import com.c103.dolbom.user.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberClientRepository extends JpaRepository<MemberClient,Long> {
    List<MemberClient> findByMemberId(Long memberId);
    List<MemberClient> findByClientId(Long clientId);
    void deleteByClientId(Long clientId);
    void deleteByClientIdAndMemberId(Long clientId,Long memberId);
    Optional<MemberClient> findByMemberIdAndClientId(Long memberId, Long clientId);

    @Query(value = "select m.id, mc.id as relationId, m.name, m.email, m.phone\n" +
            "from member_client mc join member m on (mc.client_id = m.id)\n" +
            "where mc.id in :list",nativeQuery = true)
    List<AlarmMemberInterface> getClientListByReservation(List<Long> list);
}
