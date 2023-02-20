package com.c103.dolbom.drive;

import com.c103.dolbom.drive.dto.FileResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DriveService {
    //member-client 폴더생성
    boolean memberClientFolder(Long memberClientId); //해당 상담자내담자의 폴더가 없으면 생성하는 로직 추가
    boolean pathFolder(Long memberClientId,String path) throws IOException;
    Long pathFileSave(Long memberClientId, String path, MultipartFile file) throws IOException;
    byte[] pahtFileDownload(Long fileId) throws IOException;
    List<FileResponseDto> getFileList(Long memberClientId, String path);
    List<String> getFolderList(Long memberClientId, String path);
    boolean moveFile(Long memberClientId,String path,Long id) throws IOException;
    boolean deleteFile(Long memberClientId,Long id);
    boolean deleteFolder(Long memberClientId,String path) throws IOException;//재귀 or Stream
}
