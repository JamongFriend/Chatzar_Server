# Chatzar Server

랜덤 채팅 서비스 **Chatzar**의 백엔드 서버 레포지토리입니다.  
1:1 랜덤 매칭, 친구 추가, 친구와의 1:1 기능을 제공하는 WebSocket 기반 채팅 서버입니다.

---

## 🧩 주요 기능

### 1. 매칭 및 전략적 채팅 (Core)
* **랜덤 매칭**: 실시간 큐를 이용한 1:1 매칭 및 전용 채팅방 생성
* **잠금 시스템(Locked Room)**: 친구 관계가 성립되지 않은 유저는 채팅방 이용이 제한됨
* **관계 중심 설계**: 매칭 -> 대화 탐색 -> 친구 수락 -> 잠금 해제 순의 단계적 관계 형성

### 2. 소셜 기능
* **친구 관리**: 친구 요청/수락/목록 조회
* **1:1 채팅**: WebSocket/STOMP 기반의 실시간 통신 (그룹 채팅 예정)

### 3. 보안 및 인증
* **JWT Auth**: 안전한 유저 인증 및 인가 처리
---

## 🛠 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot 4.0.0
- **Database**: MySQL 8.0, Spring Data JPA
- **Real-time**: WebSocket & STOMP
- **Security**: Spring Security, JWT (Json Web Token)
- **Build**: Gradle

---

## 📁 프로젝트 구조

```text
Project.Chatzar
├── application          # 비즈니스 로직 처리 (Service)
├── Domain               # 도메인 엔티티 및 인터페이스
│   ├── friendship       # 친구 관계 도메인
│   ├── match            # 매칭 로직 도메인
│   ├── member           # 회원 정보 도메인
│   └── chatRoom         # 채팅방 도메인
├── infrastructure       # DB 연동 및 외부 라이브러리 구현 (RepositoryImpl)
└── presentation         # 외부 노출 API 및 DTO