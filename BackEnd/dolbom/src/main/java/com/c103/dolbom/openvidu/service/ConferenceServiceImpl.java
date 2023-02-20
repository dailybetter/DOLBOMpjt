package com.c103.dolbom.openvidu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.c103.dolbom.Entity.*;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.drive.DriveRepository;
import com.c103.dolbom.drive.S3Uploader;
import com.c103.dolbom.openvidu.dto.JoinSessionDto;
import com.c103.dolbom.openvidu.dto.MemoDto;
import com.c103.dolbom.openvidu.dto.SaveMemoDto;
import com.c103.dolbom.openvidu.repository.ConferenceHistoryRepository;
import com.c103.dolbom.openvidu.repository.ConferenceRepository;
import com.c103.dolbom.openvidu.repository.MemberConferenceRepository;
import com.c103.dolbom.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ConferenceServiceImpl implements ConferenceService {

    @Autowired
    ConferenceRepository conferenceRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberConferenceRepository memberConferenceRepository;

    @Autowired
    ConferenceHistoryRepository conferenceHistoryRepository;

    @Autowired
    MemberClientRepository memberClientRepository;

    @Autowired
    DriveRepository driveRepository;

    @Value("${cloud.aws.s3.dir}")
    public String absolutePath;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷

    // 상담자가 방 생성
    @Override
    public Long createConference(Long memberId, String sessionId) {
        Member entityMember = memberRepository.findById(memberId).get();
        Conference entityConference = Conference.builder()
                .member(entityMember)
                .sessionId(sessionId)
                .build();
        Long conferenceId = conferenceRepository.save(entityConference).getId();

        MemberConference memberConference = MemberConference.builder()
                .member(entityMember)
                .conference(entityConference)
                .build();
        memberConferenceRepository.save(memberConference);
        return conferenceId;
    }

    // 내담자가 세션에 연결했을때
    // 리턴 값: memberConferenceId
    @Override
    public Long createMemberConference(JoinSessionDto dto) {
        Optional<Member> optionalClient = memberRepository.findByEmail(dto.getEmail());
        Member entityClient;
        if(optionalClient.isPresent()){
            entityClient = optionalClient.get();
        } else {
            return null;
        }
        Conference entityConference;
        Optional<Conference> optionalConference = conferenceRepository.findById(dto.getConferenceId());
        if(optionalConference.isPresent()) {
            entityConference = optionalConference.get();
        } else {
            return null;
        }
        // 세션아이디와 상담사 아이디가 일치하지 않는다면 null 반환

        if(!entityConference.getMember().getId().equals(Long.parseLong(String.valueOf(dto.getSessionId())))) {
            return null;
        }

        MemberConference memberConference = MemberConference.builder()
                .member(entityClient)
                .conference(entityConference)
                .build();
        Long memberConferenceId = memberConferenceRepository.save(memberConference).getId();

        Member entityCounselor = entityConference.getMember();

        ConferenceHistory entityConferenceHistory = ConferenceHistory.builder()
                .client(entityClient)
                .counselor(entityCounselor)
                .conference(entityConference)
                .build();

        Long conferenceHistoryId = conferenceHistoryRepository.save(entityConferenceHistory).getId();
        return memberConferenceId;
    }

    // ConfereceId에 해당하는 history들 모두에게 메모파일 텍스트 변환 후 저장
    @Override
    public int saveMemo(MemoDto dto) {
        Conference entityConference = conferenceRepository.findById(dto.getConferenceId()).get();
        // 파일 명 : 회의일자_회의시각(230206_1725.txt) -> memberclient DB를 타고 Drive에 가서 memberclientId 하위에 저장
        String date = entityConference.getCreateAt().toString();
        StringBuilder dateBuilder = new StringBuilder().append(date.substring(2,4))
                .append(date.substring(5,7)).append(date.substring(8,10)).append("_")
                .append(date.substring(11,13)).append(date.substring(14,16));

        //ConferenceId에 해당하는 conferenceHistory 목록들
        List<ConferenceHistory> conferenceHistoryList
                = conferenceHistoryRepository.findAllByConferenceId(dto.getConferenceId());
        if(conferenceHistoryList.isEmpty()){
            return 0;
        }
        for(ConferenceHistory history: conferenceHistoryList) {
            Long memberId = history.getCounselor().getId();
            Long clientId = history.getClient().getId();
            MemberClient entityMemberClient =
                    memberClientRepository.findByMemberIdAndClientId(memberId,clientId).get();
            Long memberClientId = entityMemberClient.getId();
            // 파일이 저장될 path
            String savePath = extractPath(memberClientId,"memo");

            // 회의memo -> txt 파일 저장
            File file = new File(System.getProperty("user.home") + "/"+
                    dateBuilder.toString()+"memo.txt");
            try (
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file))
            ) {
                bw.write(dto.getMemo());
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            //원래 파일 이름
            String originName = dateBuilder.toString()+"memo.txt";
            //파일의 저장이름으로 쓰일 uuid
            String uuid = UUID.randomUUID().toString();
            //확장자
            String extension = originName.substring(originName.lastIndexOf("."));
            String savedName = uuid+extension;
            String resultPath = savePath+"/"+savedName;
            amazonS3Client.putObject(new PutObjectRequest(bucket, resultPath, file).withCannedAcl(CannedAccessControlList.PublicRead));
            file.delete();
            SaveMemoDto saveMemoDto = SaveMemoDto.builder()
                    .originName(originName)
                    .savedName(savedName)
                    .path(resultPath)
                    .saveTime(LocalDateTime.now())
                    .build();
            history.saveMemo(saveMemoDto);
            conferenceHistoryRepository.save(history);
            // 드라이브에 저장
            Drive entityDrive = Drive.builder()
                    .originName(saveMemoDto.getOriginName())
                    .savedName(saveMemoDto.getSavedName())
                    .path(saveMemoDto.getPath())
                    .memberClient(entityMemberClient)
                    .build();
            driveRepository.save(entityDrive);
        }
        return 1;
    }

    private String extractPath(Long memberClientId, String path) {
        String splitRegex = Pattern.quote(System.getProperty("user.home"));
        String[] pathArr = path.split(splitRegex);
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append(memberClientId.toString());

        if(pathArr.length==1 && pathArr[0].equals("")){
            return saveFolderBuilder.toString();
        }
        for(int i=0; i<pathArr.length;i++){
            saveFolderBuilder.append("/").append(pathArr[i]);
        }

        System.out.println(saveFolderBuilder.toString());
        return saveFolderBuilder.toString();
    }

}
