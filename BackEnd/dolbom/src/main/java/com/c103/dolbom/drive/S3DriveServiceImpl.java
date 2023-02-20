package com.c103.dolbom.drive;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.c103.dolbom.Entity.Drive;
import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.drive.dto.FileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3DriveServiceImpl implements DriveService{
    private final MemberClientRepository memberClientRepository;
    private final DriveRepository driveRepository;
    private final S3Uploader s3Uploader;
    @Value("${cloud.aws.s3.dir}")
    public String absolutePath;
    @Override
    public boolean memberClientFolder(Long memberClientId) {
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append("/").append(memberClientId.toString());
        File folder = new File(saveFolderBuilder.toString());
        if(!folder.exists()){//존재x
            folder.mkdir();
            return false;
        }
        else{//이미 존재
            return true;
        }
    }

    @Override
    public boolean pathFolder(Long memberClientId, String path) throws IOException {
        String savePath = extractPath(memberClientId, path);
        s3Uploader.folderUpload(savePath);
        return true;
    }



    @Override
    public Long pathFileSave(Long memberClientId, String path, MultipartFile file) throws IOException {
        String savePath = extractPath(memberClientId, path);

        //원래 파일 이름
        String originName = file.getOriginalFilename();
        //파일의 저장이름으로 쓰일 uuid
        String uuid = UUID.randomUUID().toString();
        //확장자
        String extension = originName.substring(originName.lastIndexOf("."));
        String savedName = uuid+extension;
        String resultPath = savePath+"/"+savedName;

        String s3url = s3Uploader.fileUpload(file,resultPath);

        MemberClient memberClient = memberClientRepository.findById(memberClientId).get();

        Drive drive = Drive.builder()
                .memberClient(memberClient)
                .originName(originName)
                .savedName(savedName)
                .path(savePath+"/"+savedName)
                .build();

        Drive result = driveRepository.save(drive);

        return result.getId();
    }

    @Override
    public byte[] pahtFileDownload(Long fileId) throws IOException {
        Drive drive = driveRepository.findById(fileId).get();

        byte[] bytes = s3Uploader.fileDownload(drive.getPath());


        return bytes;
    }

    @Override
    public List<FileResponseDto> getFileList(Long memberClientId, String path) {
        String prefix = extractPath(memberClientId, path);
        List<String> fileList = s3Uploader.getLevelFileList(prefix);


        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        for(String str : fileList){
            Drive drive = driveRepository.findBySavedName(str);

            FileResponseDto dto = FileResponseDto.builder()
                    .fileId(drive.getId())
                    .fileName(drive.getOriginName())
                    .build();
            fileResponseDto.add(dto);

        }

        return fileResponseDto;
    }

    @Override
    public List<String> getFolderList(Long memberClientId, String path) {
        String prefix = extractPath(memberClientId, path);
        List<String> folderList = s3Uploader.getLevelFolderList(prefix);

        return folderList;
    }

    @Override
    public boolean moveFile(Long memberClientId, String path, Long id) throws IOException {
        Drive drive = driveRepository.findById(id).get();

        String oldPath = drive.getPath();

        String savePath = extractPath(memberClientId, path);
        String newPath = savePath + "/" + drive.getSavedName();
        s3Uploader.moveFile(oldPath,newPath);

        drive.changePath(newPath);
        driveRepository.save(drive);

        return true;
    }

    @Override
    public boolean deleteFile(Long memberClientId, Long id) {
        Drive drive = driveRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("find drive fail"));
        s3Uploader.delete(drive.getPath());
        driveRepository.deleteById(id);
        return true;
    }



    @Override
    @Transactional
    public boolean deleteFolder(Long memberClientId, String path) throws IOException {
        String savedPath = extractPath(memberClientId, path);
        List<Drive> driveList = driveRepository.findByPathStartsWith(savedPath);
        s3Uploader.deleteFolder(savedPath);
        driveRepository.deleteByPathStartsWith(savedPath);
        return true;
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
