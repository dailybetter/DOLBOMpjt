package com.c103.dolbom.openvidu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.c103.dolbom.Entity.Conference;
import com.c103.dolbom.Entity.ConferenceHistory;
import com.c103.dolbom.Entity.Drive;
import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.drive.DriveRepository;
import com.c103.dolbom.openvidu.dto.SaveMemoDto;
import com.c103.dolbom.openvidu.dto.VitoResponseDto;
import com.c103.dolbom.openvidu.repository.ConferenceHistoryRepository;
import com.c103.dolbom.openvidu.repository.ConferenceRepository;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenviduService {

    private final SttService sttService;

    @Autowired
    private ConferenceHistoryRepository conferenceHistoryRepository;

    @Autowired
    private MemberClientRepository memberClientRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    DriveRepository driveRepository;
    //    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL = "http://i8c103.p.ssafy.io:5443/";

    //    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET="MY_SECRET";
//    @Value("${OPENVIDU_URL}")
//    private String OPENVIDU_URL;
//    @Value("${OPENVIDU_SECRET}")
//    private String SECRET;
    @Value("${cloud.aws.s3.dir}")
    public String absolutePath;
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷

    private final AmazonS3Client amazonS3Client;


    private OpenVidu openVidu;

    private Map<String, String> sessionRecordingMap = new ConcurrentHashMap<>();

    static StringBuilder sttContent;

    @PostConstruct
    private void init() {
        //openvidu 서버와 연결
        this.openVidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    //녹음 시작
    public ResponseEntity<?> startRecording(String sessionId)  {
        // 이미 녹음 중일때
        if (sessionRecordingMap.get(sessionId) != null) {
            try{
                Recording recording = openVidu.getRecording(sessionRecordingMap.get(sessionId));
                if (recording.getStatus().equals(Recording.Status.started)) {
                    //녹음중
                    return new ResponseEntity<>("already recording", HttpStatus.ALREADY_REPORTED);
                }
                return new ResponseEntity<>("already recording", HttpStatus.ALREADY_REPORTED);
            }catch (OpenViduJavaClientException | OpenViduHttpException exception){
                return new ResponseEntity<>("Openvidu server error",HttpStatus.BAD_REQUEST);
            }
        }

        //새로운 녹음 시작
        Recording recording = null;
        try{
            //openvidu 서버의 녹음 데이터 확인
            recording = openVidu.getRecording(sessionId);
        }catch (OpenViduJavaClientException | OpenViduHttpException exception){
            log.info("not current Recording");
        }
        try{
            if(recording!=null && "started".equals(recording.getStatus())){
                //openvidu 서버의 녹음 데이터 확인
            }else{
                RecordingProperties properties = new RecordingProperties.Builder()
                        .outputMode(Recording.OutputMode.COMPOSED)
                        .hasAudio(true)
                        .hasVideo(false)
                        .build();
                recording = openVidu.startRecording(sessionId, properties);
            }
            sessionRecordingMap.put(sessionId, recording.getId());
            return new ResponseEntity<>("recording start",HttpStatus.OK);

        }catch (OpenViduJavaClientException | OpenViduHttpException exception){
            //녹음 시작 실패
            sessionRecordingMap.remove(sessionId);
            throw new RuntimeException();
        }
    }

    //녹음 중지
    @Transactional
    public ResponseEntity<?> stopRecording(String sessionId, Long conferenceId)  {
        try {
            //녹음 중지
            Recording recording = openVidu.stopRecording(sessionRecordingMap.get(sessionId));
            sessionRecordingMap.remove(sessionId);
            //STT 를 위한 API 전송
            String sttId = null;
            try {
                sttId = sttService.getSttId(recording.getUrl(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //ConferenceId에 해당하는 conferenceHistory 목록들
            Conference entityConference = conferenceRepository.findById(conferenceId).get();
            List<ConferenceHistory> conferenceHistoryList
                    = conferenceHistoryRepository.findAllByConference(entityConference);
            if(conferenceHistoryList.isEmpty()){
                return new ResponseEntity<>("conferenceId fail", HttpStatus.NOT_FOUND);
            }

            //stt api를 통해 받은 stt id 로 텍스트 불러오기
            sttContent = new StringBuilder();
            getSttUtterance(sttId, sttContent);

            // 파일 명 : 회의일자_회의시각(230206_1725대화기록.txt) -> memberclient DB를 타고 Drive에 가서 memberclientId 하위에 저장
            String date = LocalDateTime.now().toString();
            StringBuilder dateBuilder = new StringBuilder().append(date.substring(2,4))
                    .append(date.substring(5,7)).append(date.substring(8,10)).append("_")
                    .append(date.substring(11,13)).append(date.substring(14,16));

            //ConferenceHistory와 Drive에 DB 저장
            for(ConferenceHistory history : conferenceHistoryList) {
                Long memberId = history.getCounselor().getId();
                Long clientId = history.getClient().getId();
                MemberClient entityMemberClient =
                        memberClientRepository.findByMemberIdAndClientId(memberId,clientId).get();
                Long memberClientId = entityMemberClient.getId();
                // 파일이 저장될 path
                String savePath = extractPath(memberClientId,"STT");
                // 대화기록 -> txt 파일 저장
                File file = new File(System.getProperty("user.home") + "/"+
                        dateBuilder.toString()+"STT.txt");
                try (
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file))
                ) {
                    bw.write(sttContent.toString());
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                //원래 파일 이름
                String originName = dateBuilder.toString()+"대화기록.txt";
                //파일의 저장이름으로 쓰일 uuid
                String uuid = UUID.randomUUID().toString();
                //확장자
                String extension = originName.substring(originName.lastIndexOf("."));
                String savedName = uuid+extension;
                String resultPath = savePath+"/"+savedName;
                amazonS3Client.putObject(new PutObjectRequest(bucket, resultPath, file).withCannedAcl(CannedAccessControlList.PublicRead));
                file.delete();
                SaveMemoDto saveSttDto = SaveMemoDto.builder()
                        .originName(originName)
                        .savedName(savedName)
                        .path(resultPath)
                        .saveTime(LocalDateTime.now())
                        .build();
                history.saveStt(saveSttDto);
                conferenceHistoryRepository.save(history);
                // 드라이브에 저장
                Drive entityDrive = Drive.builder()
                        .originName(saveSttDto.getOriginName())
                        .savedName(saveSttDto.getSavedName())
                        .path(saveSttDto.getPath())
                        .memberClient(entityMemberClient)
                        .build();
                driveRepository.save(entityDrive);
            }

            return new ResponseEntity<>("stt saved finish",HttpStatus.OK);

        } catch (OpenViduJavaClientException | OpenViduHttpException exception) {
            sessionRecordingMap.remove(sessionId);
            throw new RuntimeException();
        }

    }
    //stt api를 통해 받은 stt id 로 텍스트 불러오기
    @Transactional
    public boolean getSttUtterance(String sttId, StringBuilder sb) {
        VitoResponseDto vitoResponseDto = sttService.getSttUtterance(sttId, true);
        if ("completed".equals(vitoResponseDto.getStatus())) {
            //stt 완료
            List<VitoResponseDto.Utterance> utterances = vitoResponseDto.getResults().getUtterances().stream().map(utterance -> VitoResponseDto.Utterance.builder()
                    .msg(utterance.getMsg())
                    .build()).collect(Collectors.toList());
            for(VitoResponseDto.Utterance utt : utterances) {
//                System.out.println(utt.getMsg());
                sb.append(utt.getMsg());
            }
            return true;
        }
        else if("transcribing".equals(vitoResponseDto.getStatus())) {
            getSttUtterance(sttId,sb);
        }
        return false;
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
