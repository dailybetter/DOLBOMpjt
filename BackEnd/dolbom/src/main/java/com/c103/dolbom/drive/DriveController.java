package com.c103.dolbom.drive;

import com.c103.dolbom.Entity.Drive;
import com.c103.dolbom.drive.dto.FileResponseDto;
import com.c103.dolbom.drive.dto.FolderPathRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class DriveController {
    private final DriveService driveService;
    private final DriveRepository driveRepository;
    //시큐리티 설정 : 들어올때 상담자만 들어올 수 있고, 내담자의 아이디가 존재해야한다.
    //폴더 생성 - 완
    @PostMapping("/folder")
    public ResponseEntity<?> pathFolder(@RequestBody FolderPathRequestDto dto){
        try {
            driveService.pathFolder(dto.getMemberClientId(), dto.getPath());
            return new ResponseEntity<>("create folder", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    //파일 업로드 - 완
    @PostMapping("/upload")
    public ResponseEntity<?> pathUploadFile(@RequestPart("file") MultipartFile file, @RequestPart("dto") FolderPathRequestDto dto) throws IOException {
        Long fileId = driveService.pathFileSave(dto.getMemberClientId(), dto.getPath(),file);
        return new ResponseEntity<>(fileId, HttpStatus.OK);
    }
    //파일 다운로드
    @GetMapping("/file/{id}")
    public ResponseEntity<?> download(@PathVariable("id") Long fileId, HttpServletResponse response) throws IOException {
        byte[] fileByte = new byte[0];
        try {
            fileByte = driveService.pahtFileDownload(fileId);
        } catch (IOException e) {
            return new ResponseEntity<>("byte화 실패 : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        Drive drive = driveRepository.findById(fileId).orElseThrow(() ->new IllegalArgumentException("파일을 찾을 수 없습니다."));
        String extension = drive.getOriginName().substring(drive.getOriginName().lastIndexOf("."));
       HttpHeaders httpHeaders = new HttpHeaders();
       httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
       httpHeaders.setContentLength(fileByte.length);
       String fileName = URLEncoder.encode(drive.getOriginName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
       httpHeaders.setContentDispositionFormData("attachment", fileName+extension);
        // response.setContentType("application/octet-stream");
        // response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode("tistory.png", "UTF-8")+"\";");
        // response.setHeader("Content-Transfer-Encoding", "binary");

        // response.getOutputStream().write(fileByte);
        // response.getOutputStream().flush();
        // response.getOutputStream().close();

        // return new ResponseEntity<>("성공", HttpStatus.OK);
       return new ResponseEntity<>(fileByte, httpHeaders, HttpStatus.OK);
    }

    //해당 레벨 폴더들 보여주기 - 완
    @GetMapping("/folder")
    public ResponseEntity<?> getFolderList(@RequestParam("id") Long memberClientId,@RequestParam("path") String path){
        List<String> fileList = driveService.getFolderList(memberClientId, path);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }
    //해당 레벨 파일들 보여주기 - 완
    @GetMapping("/file")
    public ResponseEntity<?> getFileList(@RequestParam("id") Long memberClientId,@RequestParam("path") String path){
        List<FileResponseDto> fileList = driveService.getFileList(memberClientId, path);

        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }
    //파일 이동 - 완
    @PatchMapping("/file")
    public ResponseEntity<?> moveFile(@RequestParam("id") Long memberClientId,@RequestParam("path") String path,@RequestParam("file_id") Long fileId){
        try {
            driveService.moveFile(memberClientId,path,fileId);
        } catch (IOException e) {
            return new ResponseEntity<>("file move fail : " + e.getMessage(), HttpStatus.OK);
        }
        return new ResponseEntity<>("file move success", HttpStatus.OK);
    }
    //파일 삭제 - 완
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("id") Long memberClientId,@RequestParam("file_id") Long fileId){
        driveService.deleteFile(memberClientId,fileId);
        return new ResponseEntity<>("file delete success", HttpStatus.OK);
    }

    //폴더 삭제 - 완
    @DeleteMapping("/folder")
    public ResponseEntity<?> deleteFolder(@RequestParam("id") Long memberClientId,@RequestParam("path") String path){
        try {
            driveService.deleteFolder(memberClientId,path);
        } catch (IOException e) {
            new ResponseEntity<>("folder delete failure", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("folder delete success", HttpStatus.OK);
    }


}
