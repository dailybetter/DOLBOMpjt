package com.c103.dolbom.drive;

import com.c103.dolbom.Entity.Drive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriveRepository extends JpaRepository<Drive, Long> {
    Drive findBySavedName(String savedName);

    List<Drive> findByPathStartsWith(String path);
    List<Drive> deleteByPathStartsWith(String path);

    Drive findDriveByMemberClientId(Long memberClientId);

}
