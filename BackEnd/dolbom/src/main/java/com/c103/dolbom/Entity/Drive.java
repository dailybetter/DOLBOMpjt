package com.c103.dolbom.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Drive extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_client_id")
    private MemberClient memberClient;

    @Column(length = 45)
    private String originName;
    @Column(length = 45)
    private String savedName;
    @Column(columnDefinition = "text")
    private String path;

    public void changePath(String path){
        this.path = path;
    }

}

