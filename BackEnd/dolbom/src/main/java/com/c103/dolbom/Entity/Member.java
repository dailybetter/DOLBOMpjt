package com.c103.dolbom.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@EntityListeners(value = AuditingEntityListener.class)
@Builder
public class Member extends BaseTimeEntity{
    @Column(nullable = false, updatable = false, length = 45,unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(columnDefinition = "text")
    private String content;

    private LocalDate birth;
    @Column(nullable = false,length = 11)
    private String phone;

    public void setUserPassword(String password) {
        this.password = password;
    }

    public void changeContent(String content){
        this.content = content;
    }
    public void changeName(String name){
        this.name = name;
    }
    public void changeBirth(LocalDate birth){
        this.birth =birth;
    }
    public void changePhone(String phone){
        this.phone=phone;
    }
}
