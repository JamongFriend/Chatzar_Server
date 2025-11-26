# 서비스 유즈케이스 정리 (Chatzar_Server)

## 1. MemberService

### 1.1 회원 가입
- 설명: 이메일/비밀번호/닉네임으로 회원 가입
- 입력: `email`, `password`, `nickname`, 프로필 정보
- 출력: 생성된 `Member` 정보
- 예외:
    - 이미 존재하는 `email`
- 메서드 시그니처 (예상):
    - `Member register(RegisterRequest dto)`

### 1.2 로그인 / 토큰 발급
- 설명: 이메일, 비밀번호로 로그인 후 JWT 발급
- 입력: `email`, `password`
- 출력: `accessToken`, `refreshToken`
- 메서드 시그니처 (예상):
    - `LoginResponse login(LoginRequest dto)`

---

## 2. MatchPreferenceService (매칭 조건 설정)

> 💡 “랜덤 매칭을 할 때 기본으로 사용할 조건을 설정/조회하는 서비스”

### 2.1 매칭 조건 생성/수정
- 설명: 유저가 마이페이지에서 매칭 조건을 설정하거나 수정
- 입력:
    - `memberId`
    - `MatchConditionDto`
        - `genderPreference` (ANY / MALE / FEMALE …)
        - `minAge`, `maxAge`
        - `topic`
        - `region`
- 동작:
    - `Member` 존재 여부 확인
    - `MatchPreference` 가 이미 있으면 → `update(condition)`
    - 없으면 → 새 `MatchPreference(member, condition)` 생성
- 출력:
    - 저장된 `MatchPreference` 또는 성공 여부
- 메서드 시그니처 (예상):
    - `void updatePreference(Long memberId, MatchConditionDto dto)`

### 2.2 내 매칭 조건 조회
- 설명: 매칭 시작 전, 현재 설정된 조건을 보여줄 때 사용
- 입력:
    - `memberId`
- 동작:
    - `MatchPreference` 조회
    - 없으면 예외 또는 기본값 반환 (정책에 따라)
- 출력:
    - `MatchConditionDto`
- 메서드 시그니처 (예상):
    - `MatchConditionDto getPreference(Long memberId)`

### 2.3 매칭 조건 초기화 (옵션)
- 설명: 매칭 조건을 기본값으로 돌릴 때
- 입력:
    - `memberId`
- 동작:
    - `MatchPreference` 삭제 또는 기본값으로 변경
- 출력:
    - 없음
- 메서드 시그니처 (예상):
    - `void resetPreference(Long memberId)`

---

## 3. MatchService (랜덤 매칭)

> 💡 “실제로 매칭 대기열에 등록하고, 상대를 찾아서 Match + ChatRoom을 만드는 서비스”

### 3.1 매칭 요청 (랜덤 매칭 시작)
- 설명: 유저가 “랜덤 매칭 시작” 버튼을 눌렀을 때 호출
- 입력:
    - `memberId`
- 동작:
    1. `MatchPreferenceService` 를 통해 해당 유저의 현재 매칭 조건 조회
        - `MatchCondition condition = matchPreferenceService.getCurrentCondition(memberId)`
    2. 조회된 `condition` 을 스냅샷으로 갖는 `MatchRequest` 생성
        - `status = WAITING`
    3. 현재 `WAITING` 상태의 다른 `MatchRequest`들과 조건 비교
    4. 조건이 맞는 상대가 있으면:
        - `Match` 생성 (`memberA`, `memberB`)
        - `ChatRoom` 생성 (`ChatRoomService.createRoom(memberA, memberB)`)
        - 두 `MatchRequest` 상태를 `MATCHED` 로 변경
    5. 없으면:
        - 그냥 `WAITING` 상태로만 두고 반환 (대기 상태)
- 출력:
    - 매칭에 성공한 경우 → `MatchResult` (매칭 정보 + ChatRoom 정보)
    - 아직 매칭 대기 중인 경우 → `MatchRequest` 정보
- 메서드 시그니처 (예상):
    - `MatchResult requestMatch(Long memberId)`

### 3.2 매칭 요청 취소
- 설명: 유저가 매칭 대기 중에 “취소”를 눌렀을 때
- 입력:
    - `memberId`
    - `matchRequestId`
- 동작:
    - 해당 요청이 `memberId`의 것이 맞는지 확인
    - `status` 가 `WAITING` 인지 확인
    - `status = CANCELED` 로 변경
- 출력:
    - 없음
- 메서드 시그니처 (예상):
    - `void cancelMatchRequest(Long memberId, Long requestId)`

### 3.3 매칭 강제 종료 (관리자/시스템)
- 설명: 신고/비정상 상태 등으로 매칭을 강제로 종료할 때
- 입력:
    - `matchId`
- 동작:
    - `Match` 조회
    - `status = ENDED` 또는 `CANCELED` 로 변경
    - 연결된 `ChatRoom` 이 있으면 `ChatRoomService.closeRoom()` 호출
- 출력:
    - 없음
- 메서드 시그니처 (예상):
    - `void endMatch(Long matchId)`

---

## 4. ChatRoomService (채팅방 관리)

> 💡 “매칭의 결과로 만들어지는 1:1 채팅방 관리”

### 4.1 채팅방 생성 (내부 유즈케이스)
- 설명: `MatchService`에서 매칭이 성사되었을 때 내부적으로 호출
- 입력:
    - `Member memberA`
    - `Member memberB`
- 동작:
    - `ChatRoom` 엔티티 생성 (`status = ACTIVE`)
    - 저장
- 출력:
    - 생성된 `ChatRoom`
- 메서드 시그니처 (예상):
    - `ChatRoom createRoom(Member memberA, Member memberB)`

### 4.2 내 채팅방 리스트 조회
- 설명: 유저가 참여한 1:1 채팅방 목록을 조회
- 입력:
    - `memberId`
- 동작:
    - `ChatRoomRepository` 에서 `memberA = memberId or memberB = memberId` 인 방들을 조회
    - 마지막 메시지, 상대 닉네임 등 요약 정보 포함해서 DTO로 변환
- 출력:
    - `List<ChatRoomSummaryDto>`
- 메서드 시그니처 (예상):
    - `List<ChatRoomSummaryDto> getMyChatRooms(Long memberId)`

### 4.3 채팅방 종료
- 설명: 둘 중 한 명이 “대화 종료”를 눌렀을 때
- 입력:
    - `chatRoomId`
    - `memberId`
- 동작:
    - 방에 속한 유저인지 확인
    - `ChatRoom.status = CLOSED` 로 변경
    - 연결된 `Match` 가 있으면 `Match.status = ENDED` 로 변경
- 출력:
    - 없음
- 메서드 시그니처 (예상):
    - `void closeRoom(Long chatRoomId, Long memberId)`

---

## 5. MessageService (메시지 전송/조회)

> 💡 “WebSocket을 통해 들어온 메시지를 검증/저장하고, 방 단위로 조회하는 서비스”

### 5.1 메시지 전송
- 설명: 클라이언트가 WebSocket으로 메시지를 보냈을 때 호출되는 도메인 로직
- 입력:
    - `chatRoomId`
    - `senderId`
    - `content`
- 동작:
    - `ChatRoom` 이 존재하는지, `status = ACTIVE` 인지 확인
    - `senderId`가 이 채팅방의 참여자인지 확인
    - `Message` 엔티티 생성 및 저장
    - 해당 채팅방을 구독 중인 사용자들에게 WebSocket으로 브로드캐스트
- 출력:
    - 저장된 `MessageDto`
- 메서드 시그니처 (예상):
    - `MessageDto sendMessage(Long chatRoomId, Long senderId, String content)`

### 5.2 메시지 조회 (히스토리 로딩)
- 설명: 유저가 채팅방에 입장했을 때 과거 메시지를 불러올 때 사용
- 입력:
    - `chatRoomId`
    - (옵션) `lastMessageId` 또는 `cursor`, `size`
- 동작:
    - 방 존재 여부와 권한 확인
    - `MessageRepository` 에서 `chatRoomId` 기준으로 정렬 조회
- 출력:
    - `List<MessageDto>`
- 메서드 시그니처 (예상):
    - `List<MessageDto> getMessages(Long chatRoomId, Long lastMessageId, int size)`

---

## 6. 전체 흐름 요약

1. 유저가 **마이페이지**에서 매칭 조건 설정  
   → `MatchPreferenceService.updatePreference(memberId, dto)`

2. 유저가 **랜덤 매칭 시작** 버튼 클릭  
   → `MatchService.requestMatch(memberId)`  
   → 내부에서 `MatchPreference` 조회 → `MatchRequest` 생성 → 매칭 시도

3. 매칭 성공  
   → `Match` + `ChatRoom` 생성  
   → 클라이언트에게 방 정보 전달 후 WebSocket 연결

4. 채팅 중  
   → 메시지 전송: `MessageService.sendMessage(chatRoomId, senderId, content)`  
   → 메시지 조회: `MessageService.getMessages(chatRoomId, ...)`

5. 대화 종료  
   → `ChatRoomService.closeRoom(chatRoomId, memberId)`  
   → `Match.status = ENDED`

