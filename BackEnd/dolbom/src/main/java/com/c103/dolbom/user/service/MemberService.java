package com.c103.dolbom.user.service;

import com.c103.dolbom.Entity.Member;
import com.c103.dolbom.user.dto.MemberDto;

public interface MemberService {

    public Long createMember(MemberDto.Basic memberDto);

    public Boolean checkIdDuplicated(String id);

    Long updateUser(Member authUser, MemberDto.Basic memberDto);

    boolean deleteUser(Member authUser);
}
