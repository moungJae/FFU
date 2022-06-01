# FFU (Friends For U)

<img src="https://user-images.githubusercontent.com/81675254/167162362-d65d5c1f-546f-489d-9c72-5fa32487acf6.png" height="400px" width="500px">

------------

## 팀원 소개
|이름|역할|
|------|---|
|조명재|프로젝트 관리, DB 구축, 애니메이션 2D 변환|
|박승규|DB 구축, 지도 API 처리 및 매핑 구현|
|김용현|UI/UX, 채팅 서버 구축|
|윤영인|UI/UX, 애니메이션 2D 변환|

------------

### 3/11 

+ 프로젝트 주제 결정

+ 주제 : 코로나 바이러스로 인해 대면 활동이 어려워진 사람들을 위한 친구 추천 APP

+ 프로젝트 설명 : 사용자의 정보(취미, 관심사, 성격, 나이, 활동 지역, ...)와 현재 위치를 기준으로 추천한다.

+ 핵심은 실제 사진을 애니메이션처럼 변환한 사진을 프로필로 설정하는 것이다.

+ 실제 사진이 아닌 애니메이션 사진으로 정함으로써 사용자의 익명성이 보장되며, 사진 도용에 대한 우려가 없다.

------------

### 3/19 
1. 프로젝트의 문제점, 해결방안
    + 사진 도용 문제 해결방안에 대한 논의

2. 개발환경 결정
    + Android App 개발 환경 -> Android Studio
    + 데이터 베이스 -> Firebase
    + 애니메이션화 -> Tensor Flow

3. Github 생성
    + https://github.com/moungJae/FFU

4. 로그인 UI
    + 각자 로그인 UI 구현
------------

### 3/26 
1. 로그인 UI 구현 리뷰
    + 각자 로그인 UI 구현한 것을 수정

2. 개발 순서, 방법
    + UI 만드는 Tool로 화면을 구성하고 구현할 지 회의
    
3. 사용자 데이터베이스
    + 회원 가입 후 어떻게 Firebase 에 업로드할지 회의
------------

### 4/2
1. Firebase에 회원 등록 구현
    
2. Login 코드 리뷰 

3. 사용지 프로필 편집
    + 프로필 편집할 때 필요한 요소가 어떤 것이 있는지 회의
------------

### 4/9
1. 기능 추가 논의
    + 사용자와 사용자를 매칭하는 것을 어떤 기준으로 하면 좋을지 회의

2. 매칭 시나리오 구성
    1. 지도에서 사용자가 클릭하면 사용자가 설정한 거리(최소 5km)내의 사용자와 일정한 기준으로 매칭  
    2. 매칭하여 사용자가 요청을 보내면 요청받은 사용자가 수락하면 대화방 자동으로 생성
------------

### 4/10
1. recommend, matching 사진 변경

2. 회원가입 후 로그인하지 않고 종료 후 다시 접속 시 자동으로 회원가입한 이메일, 비밀번호로 로그인 되는 오류 수정
------------

### 5/4
1. Animation 사진 변환 확인
    + 애니메이션 변환 과정 확인
    + 주변에 빛이 많을 경우 변환된 사진이 약간 뭉개지는 문제 확인

2. Database JSON 구조 설계
    + 회원정보, 위치, 프로필, 채팅 등을 구조화

3. Recommend 방법 결정
    + 머신 러닝을 활용하기 보다 반경 내의 모든 사용자를 추출하여 각 사용자가 원하는 대로 분류하는 기능 추가

4. User request, accept 기능
    + 사용자가 상대방으로부터 요청을 받거나 상대방에게 요청을 할 때 필요한 뷰 구조 논의

5. User recommend 기능
    + 지도 API를 활용하여 사용자 추천을 어떻게 할지 논의
------------

### 5/6
1. Profile 세팅 과정에서 mbti, personality, religion, hobby 버튼을 누르면 dialog로 선택한 data를 가져오도록 수정
    + MbitActivity.kt, PersonalActivity.kt, ReligionActivity.kt, HobbyActivity.kt 제거
    + mbti.xml, personality.xml, religion.xml, hobby.xml 제거

2. 회원가입 시 Realtime Database 에서 users, profile 내에 name 대신에 uid로 교체

3. Profile 세팅 과정에서 나이 입력하는 부분 제거
    + 회원 가입시 year-month-day 입력하는 부분에서 (현재 year) - (사용자 year) + 1 을 나이로 지정

4. Profile 화면에서 현재 로그인 한 정보를 Realtime Database 에서 이름, 소개글 등을 가져와서 동적으로 변경되도록 수정

5. Profile 사진 변경 버튼을 누르면 갤러리에 사진을 가져와서 이를 Firebase Storage 에 photo/user_uid/real.jpg 로 저장
------------

### 5/11
1. Animation-Server 에서 사용자의 request 처리
    + 다른 서비스를 이용하는 것이 아닌 public IP를 이용하고 pc를 서버로 사용
    + server 측에서는 private IP를, client 측에서는 public IP를 사용
------------

### 5/14
1. progress bar 추가
    + 로그인, 프로필 설정 시 나타나도록 하였음

2. 회원 가입 시 회원 정보를 입력한 후에 전화번호 인증 화면으로 이동하도록 변경
    + 전화번호 인증을 하기 위해 이메일, 비밀번호가 등록되어야 하는 issue 
    + 전화번호 인증 화면으로 이동 후 home 버튼을 누르면 현재 유저의 realtime database 에 삽입된 모든 data 및 이메일 계정 제거

3. Profile 설정 시 모든 데이터를 입력한 후 저장 버튼을 누르면 progress bar 가 나타나고 모든 data 가 realtime database 에 저장되면 화면에 빠져나오도록 구현
    + 프로필 사진 변경을 누르고 바로 저장 버튼을 누르면 최대 15 초 정도가 소요
------------

### 5/15
1. git merge 후 NaverMap 에러 문제 처리
    + 몇 개의 파일은 merge 되지 못한 것을 확인하고 이를 수정
    + 정상적으로 map이 띄워지는 것을 확인(zoom-in, zoom-out 및 이동이 가능)
    + background로 현재 사용자의 위치(x, y)를 실시간으로 update 해야함

2. personality, hobby 가 중복으로 들어가는 문제 해결
    + 약간의 로직 변경 및 personality 에 동일한 string 이 있는 문제를 확인 후 수정
------------

### 5/18
1. Phone verification 구현
    + 번호 발송 시 로봇체크가 뜨는 issue 발생
    + 해당 issue는 "Verifying you're not a robot..." 가 뜬 후에 메시지가 전달됨

2. Chatting 구현 완료
    + 매칭된 사용자들의 interactive communication 이 가능
------------

### 5/22
1. email, password 를 입력하여 로그인하는 방식을 phonenumber 인증을 통해 로그인하는 방식으로 변경
    + 총 3가지 패턴으로 화면이 변환됨
        1. 로그인 상태 + 회원가입 상태(모든 정보 기입) => profile 화면 이동 
        2. 로그인 상태 + 회원가입 안한 상태 => 회원가입 화면 이동
        3. 로그아웃 상태 => phonenumber 인증 화면 이동
    + CheckjoinActivity 가 가장 먼저 실행되며 로그인 상태 + join 여부를 판단하여 화면을 이동
    + phonenumber 인증이 완료되면 CheckJoinActivty 로 돌아와서 join 여부에 따라 화면이 이동 

2. 회원가입 화면의 구성은 다음과 같은 순서로 이루어짐
    + 생년월일/성별 입력 화면
    + 프로필 편집 화면

3. Json 구조를 약간 수정
    + 인물 사진인지 아닌지 판단 : Toast 문구 처리
    + server 가 정상적으로 request를 처리 : 처리가 완료될때까지 progressbar 진행
    + 프로필 변경을 마치고 저장할 경우 : 변환 사진에 permission 을 부여하여 사진을 정상적으로 띄움
------------

### 5/24
1. 번호 발송 시 로봇체크가 뜨는 issue 해결
    + Google Cloud Platform 에서 Android Device Verificaiton 을 사용 수락하여 해결

2. 애니메이션 사진으로 변환하고자 할 사진을 선택한 경우 변환 과정이 보여지도록 함
    + 사진 선택 시 해당 사진이 화면에 나타나고 "사진 변환중..." 이라는 문구를 통해 progressbar 가 진행
    + server 측에서 애니메이션 사진으로 변환한 경우 변환된 사진이 나타남
------------

### 5/27
1. Firebase database 에서 data를 가져올 때 버퍼링 문제를 해결
    + UserInformation Class 를 이용하여 해결
    + 로그인 시 UserInformation 객체를 생성함으로써 Listener 등록
        + Firebasse database 내에 존재하는 data들을 companion object의 flag, map, arrayList를 통해 data 접근
    + 로그아웃 후 동일한 번호 or 다른 번호로 로그인할 경우
        + 기존에 존재하던 listener 및 flag, map, arrayList 에 대한 초기화 
        + listener 중첩 시 overhead 가 커지는 issue 방지
        + 다른 번호로 로그인 시 이전에 로그인한 유저의 map 및 arrayList 에 저장된 data가 지속적으로 남게 되는 issue 방지
    
2. 핸드폰 인증 시, 단 한번만 요청이 가능하도록 수정
------------

### 5/28
1. UI Design
    + progress bar, button, background, 글꼴 등 변경

2. Chatting
    + 실시간 채팅시, 화면이 고정되는 문제 해결
    + 애니메이션 사진이 버퍼링 없이 정상적으로 뜨도록 해결

3. Map
    + 특정 지점에서의 유저와의 거리 계산 해결
    + 반경 내에 존재하는 유저들을 listView 에 띄우도록 함

4. UserInformation Class 리모델링
    + Profile, Animation, Recommend 에 대한 data class 를 통해 총 3가지 map의 value를 언급한 클래스 타입으로 할당이 되도록 변경
        + PROFILE : key = userId, value = Profile
        + ANIMATION : key = userId, value = Animation
        + RECOMMEND : key = userId, value = Recommend
------------

### 5/29
1. CheckLoginActivity Class 추가
    + 어플을 삭제하고 설치하여 접근할 때 dummy uid 로 로그인되는 issue 가 존재했음
    + 따라서, 실제로 존재하는 uid 인지를 check 하여 존재하지 않을 경우 Logout 처리하였음

2. Packaging
    + join package
        + CheckLoginActivity : uid 검사
        + JoinActivity : birth, gender 입력
        + PhoneVerificationActivity : phone 인증
        + WelcomeActivity : 첫 화면
    + utils package
        + Animation : animation data class
        + Profile : profile data class
        + Recommend : recommend data class
        + DBKey : db key data class

3. Code refactoring 및 annotation
    + code 간결화 및 code 설명이 필요한 부분에 대해서 annotation 추가 
    + DBKey 를 통해 firebase Realtime database 에 data 접근 처리
    + Map 을 이용해 key-value 형태로 update 하지 않고, 클래스를 객체화하여 update 하는 형태로 변경
------------
