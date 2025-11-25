# Chatjar Server

랜덤 채팅 서비스 **Chatjar**의 백엔드 서버 레포지토리입니다.  
1:1 랜덤 매칭, 친구 추가, 친구와의 1:1 / 그룹 채팅 기능을 제공하는 WebSocket 기반 채팅 서버입니다.

---

## 🧩 주요 기능

- 회원가입 / 로그인
    - 이메일 + 비밀번호 기반 회원가입
    - JWT 기반 인증/인가 (예정)
- 랜덤 매칭
    - "채팅 시작"을 누른 유저들을 큐에 넣고, 대기 중인 다른 유저와 1:1 매칭
    - 매칭 완료 시 채팅방 자동 생성
- 1:1 채팅
    - WebSocket/STOMP 기반 실시간 채팅
    - 텍스트 메시지 전송
- 친구 기능
    - 매칭이 끝난 상대를 친구로 등록
    - 친구 목록 조회
    - 친구와 채팅방 생성
- 그룹 채팅
    - 친구들을 초대하여 1:N 채팅방 생성
    - 그룹 채팅방 메시지 전송

---

## 🛠 기술 스택

- **Language**: Java
- **Framework**: Spring Boot
- **Build**: Gradle
- **DB**: (예정) MySQL / H2
- **ORM**: Spring Data JPA
- **Real-time**: Spring WebSocket + STOMP
- **Auth**: (예정) Spring Security + JWT
- **Others**: Lombok 등

---

## 📁 프로젝트 구조 (예정)

```text
src
 └─ main
    ├─ java
    │   └─ com.chatjar.server
    │       ├─ config        # WebSocket, Security 등 설정
    │       ├─ domain        # Member, ChatRoom, ChatMessage, Friend 등
    │       ├─ repository    # JPA Repository
    │       ├─ service       # 비즈니스 로직
    │       └─ controller
    │           ├─ api
