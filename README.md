
# DOLBOM

심리 상담사를 위한 온라인 상담 플랫폼

## 프로젝트를 수행하며 내가 맡았던 일

- FrontEnd 개발
- 로그인, 회원가입
- 캘린더 페이지(스케줄링 기능)
    - 내담자 생성, 스케쥴 생성, 수정, 조회, 삭제
- 파일 관리
    - 폴더와 파일 생성, 이동, 삭제, 저장, 
<br>

## DailyBetter의 프로젝트 회고

[🌱 프로젝트 전반적 회고](https://dailybetter.github.io/retrospective/projectRet/)
<br>

<!-- [👩 프로젝트를 진행하며 고민했던 것들] -->
<br>

## ✨ 프론트엔드 기술 스택 
![frontSkillStack.png](./frontSkillStack.png)
<br>

# 프로젝트 개요
- 진행기간 : 2022.01.02 ~ 2022.02.17
- 목표
    - WebRTC 기술을 사용해 비대면 화상 스터디 플랫폼 제작
    - 심리상담사를 위한 올인원 상담 플랫폼 목표
    - 일정 관리부터 상담 후 자료 관리까지 상담 프로세스 전반적인 업무 관리

<br>

- 와이어프레임
![와이어프레임.PNG](./와이어프레임.PNG)
- ERD
![dolbom_erd.png](./dolbom_erd.png)

<br>

# 프로젝트 소개
## 기획 배경
- 코로나로 인해 증가한 온라인 상담
- 심리상담사들 입장에서 편하게 사용할 수 있는 플랫폼의 필요성

# 주요 기능
## 스케줄링📰
> 스케줄을 편하게 관리
- 로그인
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950712-b9bbca11-cbc3-4107-8071-92750e34016c.gif">

<br><br><br><br>
- 내담자 신규생성
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950728-8d8a13f6-ba54-4656-80c8-daf5b1f89e37.gif">

<br><br><br><br>
- 스케줄 신규 생성
<br>
<img src="https://user-images.githubusercontent.com/61579596/219951027-86218baa-6ae6-4fd0-9585-c6e8f961ca12.gif">

<br><br><br><br>
- 스케줄 수정
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950766-e6919989-b8f8-4d4d-9d4e-ad04e8b0a4ef.gif">

<br><br><br><br>
- 스케줄 조회(월,주,일) + 드래그앤드롭으로 수정 
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950781-12d119d9-ec6a-4790-a42d-4e0e4b301cdc.gif">

<br><br><br><br>
- 스케줄 삭제
<br>
<img src="https://user-images.githubusercontent.com/61579596/219951098-031985bc-71d8-44a7-b2da-e34ef582da52.gif">

<br><br><br><br>

## 파일 관리📚
> 내담자 별로 상담 자료 편리하게 저장 가능
- 폴더 생성 + 접속
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950796-2a8df9e9-7626-4f80-bb58-da7149ff302c.gif">

<br><br><br><br>
- 폴더 이동
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950725-90b5901e-6c8c-4ab1-8403-e66397259bd3.gif">

<br><br><br><br>
- 폴더 삭제
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950792-0a6e81b9-0d26-4681-91dc-c8261efeda2f.gif">

<br><br><br><br>
- 파일 서버에 저장
<br>
<img src="https://user-images.githubusercontent.com/61579596/219950958-19a2ceb0-f466-40c0-870d-26c0c48ad722.gif">

<br><br><br><br>
- 서버에 저장된 파일 다운로드
<br>
<img src="https://user-images.githubusercontent.com/61579596/219951170-145599b3-d101-4300-9b96-14ba048f2206.gif">

<br><br><br><br>
- 서버에 저장된 파일 삭제
<br>
<img src="https://user-images.githubusercontent.com/61579596/219951276-e1195204-c8ea-4082-b1ba-1ce872672609.gif">

<br><br><br><br>
## 내담자 관리
> 내담자를 한 곳에서 관리
## 온라인 상담
> 비대면 상담 진행

<br>

# 아키텍처
![image.png](./image.png)

<br>

# 개발환경
- OS: Windows10
- Backend Framework: Spring Boot 2.7.8
- Frontend Framework: React 17
- DB: MySQL Ver 8.0.32, Redis Ver 7.0.8
- WAS: Gradle
- JVM: OpenJDK version 11
- WebRTC: openVidu 2.25.0

<br>

# 배포환경
- exec 폴더 참조

<br>

# 팀원
- 이충무 : 팀장 Backend
- 이재훈 : Backend
- 박종수 : Backend
- 박현우 : Frontend
- 박승빈 : Frontend
