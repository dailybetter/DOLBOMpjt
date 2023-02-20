package com.c103.dolbom.openvidu.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VitoResponseDto {

    private String access_token;
    private Integer expire_at;
    private String code;
    private String id;
    private String errorcode;
    private String status;
    private Results results;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Results{ // 말 리스트
        private List<Utterance> utterances;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Utterance{ // 말
        private Long start_at;
        private Long duration;
        private String msg;
        private int spk;
    }

}
