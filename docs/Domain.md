# 도메인 모델 정리 (Chatzar_Server)

## 1. Member 도메인

- **Member**
    - 유저 기본 정보 및 프로필
    - MatchRequest, Match, ChatRoom, Message 등에서 참조
---
## 2. Match 도메인

### 2.1 MatchCondition (@Embeddable)
- 매칭 조건 값 객체
- 필드:
    - genderPreference
    - minAge / maxAge
    - topic
    - region
- 관계: MatchRequest —–(1:1 포함, 별도 테이블 X)——> MatchCondition

### 2.2 MatchRequest
- 유저가 “랜덤 매칭 시작” 할 때 생성되는 요청
- 필드:
    - requester (Member)
    - condition (MatchCondition)
    - status (WAITING, MATCHED, CANCELED, TIMEOUT)
    - createdAt
- 관계:
    - Member (1) ← (N) MatchRequest
    - MatchRequest (1) o– (1) MatchCondition
### 2.3 Match
- 실제로 두 유저가 매칭된 결과
- 필드:
    - memberA, memberB (Member)
    - status (ACTIVE, ENDED, CANCELED)
    - createdAt, endedAt
    - chatRoomId (ChatRoom 참조)
- 관계:
    - Member (1) ← (N) Match.memberA
    - Member (1) ← (N) Match.memberB
    - Match (1) → (1) ChatRoom
---
## 3. Chat 도메인

### 3.1 ChatRoom
- 1:1 채팅방
- 필드:
    - memberA, memberB (Member)
    - status (ACTIVE, CLOSED)
    - createdAt
- 관계: 
    - ChatRoom (1) ← (N) Message : 한 방에 여러 메시지
    - Member 와는 “이 방에 누가 있는가” 관계 (현재는 필드로 직접 가지고 있음)
### 3.2 Message
- 채팅 메시지
- 필드:
    - chatRoom (ChatRoom)
    - sender (Member)
    - content
    - createdAt
- 관계:
    - ChatRoom (1) ← (N) Message
    - Member (1) ← (N) Message.sender
---
## 4. 간단 관계 다이어그램

- Member 1 : N MatchRequest
- MatchRequest 1 : 1 MatchCondition (@Embedded)
- Member 1 : N Match (memberA, memberB)
- Match 1 : 1 ChatRoom
- ChatRoom 1 : N Message
- Member 1 : N Message (sender)
