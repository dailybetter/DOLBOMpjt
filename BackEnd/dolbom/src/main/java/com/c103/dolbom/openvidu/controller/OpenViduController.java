package com.c103.dolbom.openvidu.controller;

import java.util.Map;

import javax.annotation.PostConstruct;

import com.c103.dolbom.openvidu.service.OpenviduService;
import com.c103.dolbom.user.auth.PrincipalDetails;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OpenViduController {

    private final OpenviduService openviduService;

//    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL = "http://i8c103.p.ssafy.io:5443/";

//    @Value("${OPENVIDU_SECRET}")
    private String OPENVIDU_SECRET="MY_SECRET";

    private OpenVidu openvidu;


    @PostConstruct
    public void init() {
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

    /**
     * @param params The Session properties
     * @return The Session ID
     */
    @PostMapping("/api/sessions")
    public ResponseEntity<String> initializeSession(@RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        SessionProperties properties = SessionProperties.fromJson(params).build();
        Session session = openvidu.createSession(properties);
        return new ResponseEntity<>(session.getSessionId(), HttpStatus.OK);
    }

    /**
     * @param sessionId The Session in which to create the Connection
     * @param params    The Connection properties
     * @return The Token associated to the Connection
     */
    @PostMapping("/api/sessions/{sessionId}/connections")
    public ResponseEntity<String> createConnection(@PathVariable("sessionId") String sessionId,
                                                   @RequestBody(required = false) Map<String, Object> params)
            throws OpenViduJavaClientException, OpenViduHttpException {
        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ConnectionProperties properties = ConnectionProperties.fromJson(params).build();
        Connection connection = session.createConnection(properties);
        return new ResponseEntity<>(connection.getToken(), HttpStatus.OK);
    }


    //녹음 시작
    @GetMapping("/api/openvidu/recordings/start")
    public ResponseEntity<?> startRecording(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String sessionId = String.valueOf(principalDetails.getMember().getId());
        return openviduService.startRecording(sessionId);
    }
    //녹음 중지
    @GetMapping("/api/openvidu/recordings/stop/{conferenceId}")
    public ResponseEntity<?> stopRecording(@PathVariable("conferenceId")String conferenceId,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long id = Long.parseLong(String.valueOf(conferenceId));
        String sessionId = principalDetails.getMember().getId().toString();
        return openviduService.stopRecording(sessionId, id);
    }

}
