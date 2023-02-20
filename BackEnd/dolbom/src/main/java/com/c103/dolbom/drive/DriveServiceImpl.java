package com.c103.dolbom.drive;

import com.c103.dolbom.Entity.Drive;
import com.c103.dolbom.Entity.MemberClient;
import com.c103.dolbom.client.MemberClientRepository;
import com.c103.dolbom.drive.dto.FileResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//로컬 저장
//@Service
@RequiredArgsConstructor
@Slf4j
public class DriveServiceImpl implements DriveService{
    private final MemberClientRepository memberClientRepository;
    private final DriveRepository driveRepository;
    private final String absolutePath = File.separator + "home" + File.separator + "ubuntu" + File.separator + "Dolbom";
    @Override
    public boolean memberClientFolder(Long memberClientId) {
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append(File.separator).append(memberClientId.toString());
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
    public boolean pathFolder(Long memberClientId, String path) {
        String savePath = extractPath(memberClientId, path);

        File folder = new File(savePath);

        if(!folder.exists()){//존재x
            folder.mkdir();
            return true;
        }
        else{//이미 존재
            return false;
        }
    }

    private String extractPath(Long memberClientId, String path) {
        String splitRegex = Pattern.quote(System.getProperty("file.separator"));
        String[] pathArr = path.split(splitRegex);
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append(File.separator).append(memberClientId.toString());

        for(int i=0; i<pathArr.length;i++){
            saveFolderBuilder.append(File.separator).append(pathArr[i]);
        }
        return saveFolderBuilder.toString();
    }

    @Override
    public Long pathFileSave(Long memberClientId, String path, MultipartFile file) throws IOException {
        String savePath = extractPath(memberClientId, path);
        File folder = new File(savePath);
        if (!folder.exists()){
            return -1L;
        }
        //원래 파일 이름
        String originName = file.getOriginalFilename();
        //파일의 저장이름으로 쓰일 uuid
        String uuid = UUID.randomUUID().toString();
        //확장자
        String extension = originName.substring(originName.lastIndexOf("."));
        String savedName = uuid+extension;

        MemberClient memberClient = memberClientRepository.findById(memberClientId).get();

        Drive drive = Drive.builder()
                .memberClient(memberClient)
                .originName(originName)
                .savedName(savedName)
                .path(savePath+File.separator+savedName)
                .build();

        file.transferTo(new File(savePath+File.separator+savedName));

        Drive result = driveRepository.save(drive);

        return result.getId();
    }

    @Override
    public byte[] pahtFileDownload(Long fileId) throws IOException {
        Drive drive = driveRepository.findById(fileId).get();
        File file = new File(drive.getPath());

        return null;
    }

    @Override
    public List<FileResponseDto> getFileList(Long memberClientId, String path) {
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append(File.separator).append(memberClientId.toString());
        File rootFolder = new File(saveFolderBuilder.toString());
        if(!rootFolder.exists()){//존재x
            rootFolder.mkdir();
        }

        String savePath = extractPath(memberClientId, path);
        File folder = new File(savePath);
        String[] fileList = folder.list();

        List<FileResponseDto> fileResponseDto = new ArrayList<>();

        for(String str : fileList){
            File file = new File(savePath,str);
            //파일인 경우
            if(file.isFile()){
                //repository에서 가져오는 방식
                Drive drive = driveRepository.findBySavedName(str);

                FileResponseDto dto = FileResponseDto.builder()
                        .fileId(drive.getId())
                        .fileName(drive.getOriginName())
                        .build();
                fileResponseDto.add(dto);
            }

        }

        return fileResponseDto;
    }

    @Override
    public List<String> getFolderList(Long memberClientId, String path) {
        log.info("absolutePath " +absolutePath);
        log.info(new File("").getPath());
        StringBuilder saveFolderBuilder = new StringBuilder();
        saveFolderBuilder.append(absolutePath).append(File.separator).append(memberClientId.toString());
        File rootFolder = new File(saveFolderBuilder.toString());
        log.info("getFolderList " + rootFolder.getName());
        log.info("루트 폴더 존재하나? " + rootFolder.exists() +" 절대 경로의 절대경로" + new File(absolutePath).getAbsolutePath());
        log.info("절대 경로의 부모 폴더 " + new File(absolutePath).getParent());
        if(!rootFolder.exists()){//존재x
            rootFolder.mkdirs();
        }
        log.info( "루트 폴더는 폴더인가? " + new File(rootFolder.getAbsolutePath()).isDirectory());
        log.info("루트 폴더 존재하나? " + rootFolder.exists() +" 루트 폴더의 절대경로" + new File(rootFolder.getAbsolutePath()).getAbsolutePath());
        String savePath = extractPath(memberClientId, path);
        File folder = new File(savePath);
        String[] fileList = folder.list();
        List<String> folderNameList = new ArrayList<>();
        for(String str : fileList){
            File file = new File(savePath,str);
            //폴더인 경우
            if(file.isDirectory()){
                folderNameList.add(str);

            }
        }
        return folderNameList;

    }

    @Override
    public boolean moveFile(Long memberClientId, String path, Long id) throws IOException {
        Drive drive = driveRepository.findById(id).get();

        Path oldPath = Paths.get(drive.getPath());

        String savePath = extractPath(memberClientId, path);
        Path newPath = Paths.get(savePath + File.separator + drive.getSavedName());

        Files.move(oldPath,newPath, StandardCopyOption.ATOMIC_MOVE);

        drive.changePath(savePath + File.separator + drive.getSavedName());
        driveRepository.save(drive);

        return true;
    }

    @Override
    public boolean deleteFile(Long memberClientId, Long id) {
        Drive drive = driveRepository.findById(id).get();
        File file = new File(drive.getPath());
        driveRepository.deleteById(id);
        file.delete();

        return true;
    }



    @Override
    @Transactional
    public boolean deleteFolder(Long memberClientId, String path) throws IOException {
        String savePath = extractPath(memberClientId, path);
        List<Drive> driveList = driveRepository.findByPathStartsWith(savePath);
        driveRepository.deleteByPathStartsWith(savePath);

        File baseDir = new File(savePath);
        Files.walk(baseDir.toPath()).sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach((file) -> {
                    file.delete();
                });



        return true;
    }



}
