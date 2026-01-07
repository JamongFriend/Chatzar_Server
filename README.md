# Chatzar Server

랜덤 채팅 서비스 **Chatzar**의 백엔드 서버 레포지토리입니다.  
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
- 그룹 채팅 (예정)
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
- **Auth**: Spring Security + JWT
- **Others**: Lombok 등

---

## 📁 프로젝트 구조

```text
src
src
 └─ main
    ├─ java
    │   └─ Project.Chatzar
    │       ├─ config
    │       │   ├─ WebSocketConfig          # WebSocket(STOMP) 설정 (/ws, /pub, /sub)
    │       │   ├─ StompAuthInterceptor     # STOMP 연결 시 JWT 인증 처리
    │       │   ├─ StompPrincipal           # WebSocket 세션용 Principal(memberId)
    │       │   └─ SecurityConfig           # Spring Security 설정
    │       │
    │       ├─ domain
    │       │   ├─ member                   # Member 엔티티 및 도메인 로직
    │       │   ├─ chatRoom                 # ChatRoom 엔티티
    │       │   ├─ message                  # Message(채팅 메시지) 엔티티
    │       │   ├─ match                    # 매칭 관련 도메인
    │       │   └─ chatEvent                # JOIN / LEAVE 등 채팅 이벤트 로그
    │       │
    │       ├─ infrastructure
    │       │   ├─ member                   # MemberRepository (도메인 인터페이스)
    │       │   ├─ chatRoom                 # ChatRoomRepository
    │       │   ├─ message                  # MessageRepository
    │       │   ├─ match                    # MatchRepository
    │       │   └─ chatEvent                # ChatEventRepository
    │       │
    │       ├─ application
    │       │   ├─ AuthService              # 인증 / JWT 발급 및 검증
    │       │   ├─ MatchService             # 랜덤 매칭 로직
    │       │   ├─ ChatRoomService           # 채팅방 생성 / 조회 / 종료
    │       │   ├─ MessageService            # 메시지 송신 / 조회
    │       │   └─ ChatEventService          # JOIN / LEAVE 이벤트 로그 처리
    │       │
    │       ├─ presentation
    │       │   ├─ controller
    │       │   │   ├─ api
    │       │   │   │   ├─ AuthController       # 회원 인증 API
    │       │   │   │   ├─ MatchController      # 매칭 API
    │       │   │   │   ├─ ChatRoomController   # 채팅방 API
    │       │   │   │   └─ MessageController    # 메시지 조회 API
    │       │   │   │   └─ MemberController    
    │       │   │   │
    │       │   │   └─ webSocket
    │       │   │       └─ ChatStompController  # 실시간 채팅(STOMP) 처리
    │       │   │
    │       │   └─ dto
    │       │       ├─ auth                    # 인증 관련 DTO
    │       │       ├─ match                   # 매칭 DTO
    │       │       ├─ chatroom                # 채팅방 DTO
    │       │       └─ message                 # 메시지 / STOMP DTO
    │       │       └─ member                  # 유저 프로필용 DTO
    │       │
    │       └─ legacy
    │           └─ socket                     # 기존 ClientHandler 기반 소켓 코드(미사용)
    │
    └─ resources
        ├─ application.yml                   # 애플리케이션 설정
        └─ static
