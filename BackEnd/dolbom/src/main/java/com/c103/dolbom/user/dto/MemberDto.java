package com.c103.dolbom.user.dto;


import com.c103.dolbom.Entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @AllArgsConstructor
    public static class Id {
        private Long memberId;
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    public static class Basic {
        Long id;

        String email;

        String password;

        String name;

        String role;

        String content;

        String birth;

        String phone;
    }
}
