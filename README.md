# 🌳 저탄소 녹색 Life

## ![4조_저탄소 그린 Life-이미지-0](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/7786d8db-2a75-4ee1-9cdd-98a166669ed6)

## ⭐️ 프로젝트 소개

- <strong>전기량 / 가스량 대시보드</strong> : 직관적으로 에너지 사용 통계를 확인할 수 있습니다
- <strong>저탄소 실천 챌린지</strong> : 저탄소 생활을 실천해보세요
- <strong>에코 Mall</strong> : 친환경 에코 아이템을 구매할 수 있습니다
- <strong>저탄소 Life 공유</strong> : 저탄소 생활을 다른 사람들과 공유할 수 있습니다

## ⭐️ 시연 영상

[![Video Label](http://img.youtube.com/vi/btZ3BM3Ykkk/sddefault.jpg)](https://youtu.be/btZ3BM3Ykkk)

## ⭐️ 개발 환경

<p>🔶 Front-End 🔶</p>
<div>
  <!--Html5-->
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white"/>
  <!--Css-->
  <img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=CSS3&logoColor=white"/>
  <!--javascript-->
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=JavaScript&logoColor=white"/>
  <!--JQuery-->
  <img src="https://img.shields.io/badge/JQuery-0769AD?style=flat&logo=jQuery&logoColor=white"/>
  <!--Thymeleaf-->
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white">
  <!--BootStrap-->
  <img src="https://img.shields.io/badge/bootstrap-7952B3?style=flat&logo=bootstrap&logoColor=white">

  <br>

  <p>🔶 Back-End 🔶</p>
  <!--Spring Boot-->
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat&logo=springboot&logoColor=white">
  <!--Spring Security-->
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat&logo=springsecurity&logoColor=white">
  <!--Spring JPA-->
  <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat&logo=spring&logoColor=white">
  <!--Maria DB-->
  <img src="https://img.shields.io/badge/Spring Data JPA-003545?style=flat&logo=spring&logoColor=white">

  <br>

  <p>🔶 Others 🔶</p>
   <!--Github-->
  <img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white">
  <!--Figma-->
  <img src="https://img.shields.io/badge/figma-F24E1E?style=flat&logo=figma&logoColor=white">
  <!--Figma-->
  <img src="https://img.shields.io/badge/notion-000000?style=flat&logo=notion&logoColor=white">

</div>

## ⭐️ 프로젝트 구조
```
EchoProject
├─src
│  ├─main
│  │  ├─java
│  │  │  └─com.project.echoproject
│  │  │     ├─config
│  │  │     ├─controller
│  │  │     ├─dto
│  │  │     │  └─order
│  │  │     ├─email
│  │  │     ├─entity
│  │  │     ├─exception
│  │  │     ├─oauth
│  │  │     ├─repository
│  │  │     ├─service
│  │  │     └─valid
│  │  │
│  │  └─resources
│  │     ├─static
│  │     │  ├─css
│  │     │  │  ├─account
│  │     │  │  ├─authBoard
│  │     │  │  ├─common
│  │     │  │  ├─order
│  │     │  │  └─...
│  │     │  ├─js
│  │     │  │  ├─authBoard
│  │     │  │  ├─chatBot
│  │     │  │  ├─common
│  │     │  │  └─...
│  │     │  ├─img
│  │     │  └─json
│  │     │
│  │     └─templates
│  │        ├─account
│  │        ├─admin
│  │        ├─authBoard
│  │        ├─error
│  │        ├─order
│  │        └─...
│  │
│  └─test
│     └─java
│        └─com.project.echoproject
│           └─service
│
├─build
│  ├─classes
│  ├─generated
│  ├─resources
│  └─tmp
│
├─gradle
│
└─uploads
```

## ⭐️ 역할 분담

### 👩 하 가 영
* UI
    * 메인페이지 CSS 개선
    * 쿠폰 보유 현황 페이지 구현
* 기능
    * 공공데이터 자료 가공 및 차트 구현
    * 사용자가 입력한 데이터로 통계 구현
* 팀원과의 의견 조율
* 시연 영상 제작

### 🧑 김 영 준
* UI
    * 메인 로고 제작
    * 메인 페이지 CSS 개선 및 레이아웃 페이지 제작
    * 환경 알리미 뉴스 게시판 CSS 개선
    * 공지사항 게시판 CSS 개선
    * 인증 게시판 목록, 상세글 보기, 신고 페이지 제작
    * 에코 Mall 상품 목록, 상품 상세, 구매 페이지, 결제 완료 페이지, 주문 내역 페이지 제작
    * 로그인 및 회원가입 페이지 제작
* 기능
    * 네이버 클로바 API를 이용한 챗봇 구현
    * 아임포트 API를 이용한 카카오페이 결제 기능 구현
    * Oauth2.0을 활용한 소셜로그인 기능 구현
    * JavaMail API와 Google SMTP를 이용한 이메일 인증 시스템 구축
    * TensorFlow.js를 활용한 이미지 분석 기능 구현
    * 인증게시판 CRUD 기능 구현 및 좋아요 기능, 신고 기능
    * 아이디 찾기 기능 구현 및 비밀번호 찾기 기능 구현
    * 회원가입 기능 구현
    * SpringSecurity, OAuth2.0을 통한 보안 기능 구현

### 👩 변 하 영
* UI
    * 로그인, 회원가입 페이지 참여
* 기능 
    * 회원 가입 유효성 검사
* API 자료 찾기 및 템플릿 자료 검색
* 시연 영상 대본 제작
  
### 👩 유 혜 린
* UI
    * 챌린지 현황 페이지 구현
    * 쿠폰 mall 페이지 구현
    * 관리자 사이트 메인화면 제작
* 기능
    * 관리자 기능 구현
    * 챌린지 관리 기능 구현
    * 신고 관리 구현

### 👩 유 혜 원
* UI
    * 공지사항 목록 페이지 참여
* 기능
    * 에코 뉴스 페이지 네이버뉴스 API 연동
    * 공지사항(메인/관리자)페이지 CRUD 기능 구현 및 관리자만 C,U,D 권한 처리
* 시연 영상 제작 및 영상 대본 제작

### 👩 정 유 진
* UI
    * 마이페이지 제작
* 기능
    * 프로필 페이지, 개인정보 수정 페이지, 비밀번호 변경 페이지, 탈퇴 기능 구현
    * 전기 / 가스 사용량 입력, 상세보기 페이지 구현
* API 자료 찾기 및 템플릿 자료 검색
* 영상 대본 및 플로우 차트 제작

## ⭐️ 개발 기간
* <strong>전체 개발 기간</strong> : 2024-06-17 ~ 2024-07-01
* <strong>기획 및 설계</strong> : 2024-06-17 ~ 2024-06-20
* <strong>기능 구현</strong> : 2024-06-21 ~ 2024-07-01

## ⭐️ 주요 기능

### 🌈 대시보드
![대시보드](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/8b7d7211-5c39-4b42-9e4b-c78de8ea7c90)

* 서울 및 경기도 시군구별 전기 및 가스 사용량 통계
* 사용자가 입력한 현재 달의 사용량과 전월 사용량 통계 및 간단한 피드백 표시

### 🌈 챗봇
![스크린샷 03-07-2024 20 48 22](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/86b65f34-e738-4137-8426-9ddb5ef3bba0)

* 사용자가 궁금한 점을 입력하면, 챗봇이 해당 질문에 대해 적절한 대답을 제공 

### 🌈 재활용 도우미
![스크린샷 03-07-2024 20 53 04](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/ae584c2e-266c-497b-b713-d27bc0666f18)

* 사용자가 재활용하고자 하는 쓰레기 사진을 업로드하면 이미지를 분석하여 적절한 해결법 제시

### 🌈 친환경 에코 Mall
![그림1](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/e24c664a-fc37-4b4e-b48d-5b91011647fc) 

![스크린샷 03-07-2024 20 58 07](https://github.com/GSITMTeamproject4/Team4project/assets/121008744/a2712a6b-1443-41cf-af6c-7dcde7b49b46)

* 사용자가 원하는 제품을 장바구니에 추가하고 카카오페이로 결제 진행

## ⭐️ 프로젝트 후기

### 🌈 잘한 점

* 챌린지와 쇼핑몰 등 다양한 기능을 구현하여 다채로운 웹사이트를 제작할 수 있었다
* 스프링 시큐리티를 통해 사용자 인증과 권한 관리를 체계적으로 처리함으로써, 프로젝트의 보안성을 강화할 수 있었다

### 🌈 아쉬운 점

* 협업 과정에서 일의 분배가 제대로 이루어지지 않은 점이 아쉬웠다
* 프로젝트 진행중 에러 코드와 코드 주석과 같은 문서화에 충분히 신경을 쓰지 못한 부분이 아쉬웠다
* 추가적으로 구현하고 싶은 기능(ex. 실시간 채팅 등)을 구현 못한 점이 아쉬웠다







