package com.c103.dolbom.alarm.service;

import com.c103.dolbom.Entity.Schedule;
import com.c103.dolbom.alarm.dto.AlarmMemberInterface;
import com.c103.dolbom.alarm.dto.MessageDto;
import com.c103.dolbom.alarm.dto.SmsRequest;
import com.c103.dolbom.alarm.dto.SmsResponse;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.schedule.repository.ScheduleRepository;
import com.c103.dolbom.user.dto.MemberDto.Basic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {

    @Value("${sms.serviceId}")
    private String serviceId;
    @Value("${sms.accessKey}")
    private String accessKey;
    @Value("${sms.secretKey}")
    private String secretKey;

    private final ScheduleRepository scheduleRepository;
    private final MemberClientRepository memberClientRepository;

    @Override
    @Scheduled(cron = "0 0 0 1/1 * ?") // 매일 1번씩 보내기
    public void selectSchedule() throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {

        // 1. 당일예약 찾기
        String curTime = LocalDateTime.now().toString();
        List<Schedule> scheduleByTime = scheduleRepository.getScheduleByTime(curTime, 24);

        // 2. 관계 id로 유저 찾기
        Map<Long, Schedule> relationIdMap = new HashMap<>();

        for(Schedule s : scheduleByTime) {
            relationIdMap.put(s.getId(), s);
        }

        // 관계 ID, 내담자 정보
        Map<Long, Basic> clientMap = new HashMap<>();

        memberClientRepository.getClientListByReservation(new ArrayList<>(relationIdMap.keySet()))
                .stream()
                .map((AlarmMemberInterface id) -> clientMap.put(id.getRelationId(), Basic.builder().
                        id(id.getId())
                        .name(id.getName())
                        .phone(id.getPhone())
                        .email(id.getEmail())
                        .build()))
                .collect(Collectors.toList());

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for(Long l : clientMap.keySet()) {
            Schedule s = relationIdMap.get(l);
            Basic b = clientMap.get(l);

            sb1.append(b.getName()).append("님 상담 예정입니다.");
            sb2.append("상담 시간은 ").append(s.getStartTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")))
                    .append("입니다");

            sendSms(b.getPhone(), sb1.toString());
            sendSms(b.getPhone(), sb2.toString());

            sb1.setLength(0);
            sb2.setLength(0);
        }

    }

    @Override
    public SmsResponse sendSms(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, URISyntaxException {
        Long time = System.currentTimeMillis();
        List<MessageDto> messages = new ArrayList<>();
        messages.add(new MessageDto(recipientPhoneNumber, content));

        SmsRequest smsRequest = new SmsRequest("SMS", "COMM", "82", "01022326243", "내용", messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", this.accessKey);
        String sig = makeSignature(time); //암호화
        headers.set("x-ncp-apigw-signature-v2", sig);

        HttpEntity<String> body = new HttpEntity<>(jsonBody,headers);
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        SmsResponse smsResponse = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+this.serviceId+"/messages"), body, SmsResponse.class);

        return smsResponse;
    }

    @Override
    public String makeSignature(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}