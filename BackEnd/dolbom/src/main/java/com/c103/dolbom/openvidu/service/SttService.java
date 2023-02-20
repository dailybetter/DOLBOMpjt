package com.c103.dolbom.openvidu.service;

import com.c103.dolbom.openvidu.dto.MultipartInputStreamFileResource;
import com.c103.dolbom.openvidu.dto.VitoConfigDto;
import com.c103.dolbom.openvidu.dto.VitoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class SttService {

    private String STT_TOKEN;

    @PostConstruct
    private void init() {
        getSttToken();
    }

    @Value("${vitto.id}")
    private String VITTO_ID;

    @Value("${vitto.secret}")
    private String VITTO_SECRET;

    //vito token 불러오기
    public void getSttToken(){
        System.out.println("getSttToken, " +STT_TOKEN+ " -> ");
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        HttpStatus httpStatus = HttpStatus.CREATED;
        RestTemplate restTemplate = new RestTemplate();
        map.add("client_id", VITTO_ID);
        map.add("client_secret", VITTO_SECRET);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String url = "https://openapi.vito.ai/v1/authenticate";
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        VitoResponseDto responseDto = restTemplate.postForObject(url, requestEntity, VitoResponseDto.class);
        STT_TOKEN = "bearer " + responseDto.getAccess_token();
//        System.out.println(STT_TOKEN);
    }

    //음성 파일로 vito sttId 받아오기
    public String getSttId(String fileUrl, boolean resend) throws IOException {
        // resend 반복 토큰 요청 막기
//        getSttToken();
        System.out.println("getSttId, token = " + STT_TOKEN);
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        HttpStatus httpStatus = HttpStatus.CREATED;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", STT_TOKEN);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        map.add("file", new MultipartInputStreamFileResource(new URL(fileUrl).openStream(), "send.mp4"));
        map.add("config", new VitoConfigDto());
        String url = "https://openapi.vito.ai/v1/transcribe";
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        try {
            VitoResponseDto vitoResponseDto = restTemplate.postForObject(url, requestEntity, VitoResponseDto.class);

            return vitoResponseDto.getId();
        } catch (Exception e) {
            //토큰 만료시 토큰 불러오고 본 함수 다시 호출
            if (resend) {
                //한번만 재호출
                getSttToken();
                return getSttId(fileUrl, false);
            } else {
                throw new RuntimeException();
            }
        }
    }

    //vito sttId로 변환된 텍스트 받아오기
    public VitoResponseDto getSttUtterance(String sttId, boolean resend){
        // resend 반복 토큰 요청 막기
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        HttpStatus httpStatus = HttpStatus.CREATED;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", STT_TOKEN);
//        System.out.println("getSttUtterance");

        String url = "https://openapi.vito.ai/v1/transcribe/"+sttId;
//        System.out.println("sttId: " + sttId);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        try {

            ResponseEntity<VitoResponseDto> vitoResponseDtoResponseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity , VitoResponseDto.class);

            VitoResponseDto vitoResponseDto = vitoResponseDtoResponseEntity.getBody();

            return vitoResponseDto;
        }catch (Exception e){
            //토큰 만료시 토큰 불러오고 본 함수 다시 호출
            if(resend){
                //한번만 재호출
                getSttToken();
                return getSttUtterance(sttId,false);
            }else {
                throw new RuntimeException();
            }
        }

    }


}
