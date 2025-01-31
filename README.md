# 항해 플러스 백엔드 2주차 과제
## 1. [ 2주차 과제 ] 특강 신청 서비스
<details>  
<summary>열기</summary>
- `특강 신청 서비스`를 구현해 봅니다.
- 항해 플러스 토요일 특강을 신청할 수 있는 서비스를 개발합니다.
- **특강 신청 및 신청자 목록 관리를 RDBMS를 이용해 관리할 방법을 고민합니다.**

### Requirements

- 아래 2가지 API 를 구현합니다.
  - 특강 신청 API
  - 특강 신청 여부 조회 API
- 각 기능 및 제약 사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려 하여 구현합니다.
- **DB 는 MySQL / MariaDB 로 제한합니다.**
- **Test 는 (1) 인메모리 DB (2) docker-compose 정도 허용합니다. ( + TestContainers 이용해도 됨 )**

### API Specs

1️⃣ **(핵심)** 특강 신청 **API**

- 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
- 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있습니다.
- 특강은 선착순 30명만 신청 가능합니다.
- 이미 신청자가 30명이 초과 되면 이후 신청자는 요청을 실패합니다.

**2️⃣ 특강 신청 가능 목록 API**

- 날짜별로 현재 신청 가능한 특강 목록을 조회하는 API 를 작성합니다.
- 특강의 정원은 30명으로 고정이며, 사용자는 각 특강에 신청하기 전 목록을 조회해 볼 수 있어야 합니다.

3️⃣  **특강 신청 완료 목록 조회 API**

- 특정 userId 로 신청 완료된 특강 목록을 조회하는 API 를 작성합니다.
- 각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 합니다.

💡 **KEY POINT**

- 정확하게 30 명의 사용자에게만 특강을 제공할 방법을 고민해 봅니다.
- 같은 사용자에게 여러 번의 특강 슬롯이 제공되지 않도록 제한할 방법을 고민해 봅니다.


**이것부터 시작하세요!**

- 아키텍처 준수를 위한 애플리케이션 패키지 설계
- 특강 도메인 테이블 설계 (ERD) 및 목록/신청 등 기본 기능 구현
- 각 기능에 대한 **단위 테스트** 작성
- _사용자 회원가입/로그인 기능은 구현하지 않습니다._

### **`STEP 3`**

- 설계한 테이블에 대한 **ERD** 및 이유를 설명하는 **README** 작성
- 선착순 30명 이후의 신청자의 경우 실패하도록 개선
- 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증하는 **통합 테스트** 작성

### **`STEP 4`**

- 같은 사용자가 동일한 특강에 대해 신청 성공하지 못하도록 개선
- 동일한 유저 정보로 같은 특강을 5번 신청했을 때, 1번만 성공하는 것을 검증하는 **통합 테스트** 작성
</details>

## 2. ERD 및 설계 시 고려사항
![특강 신청 서비스.png](https://github.com/user-attachments/assets/e7081fed-8880-4bf9-8afc-e314aff37141)

### 요구사항 분석 및 정리
- 특강 신청 서비스 다음 3가지 기능을 지원한다.
  - 사용자는 특강을 신청한다 (POST `/lectures/{lectureId}/registration`)
  - 사용자는 신청이 가능한 특강 목록을 조회한다 (GET `/lectures/available`)
  - 사용자는 신청을 완료한 특강 목록을 조회한다 (GET `/lectures/registered`)
- 이 떄, 데이터 설계 관점에서는 다음의 2개의 Entity 와 1개의 Relationship을 도출했다.
  - [E] 사용자(User) : 특별히 담아야 하는 정보가 없음
  - [E] 특강 (Lecture) : 이름, 강연자ID, 특강일시
  - [R] 신청 (Lecture Registration) : 특강ID, 신청ID, 신청일시
- 추가적으로 정책으로는 다음의 사항들을 고려했다.
  - 특강의 정원은 항상 30명이며, 선착순으로 신청을 받는다.
  - 동일한 신청자는 동일한 강의에 대해서 한 번의 수강 신청만 성공할 수 있다.
  - 강연자는 한 번에 하나의 특강만 진행할 수 있다.
    - 강연자와 특강일시가 동일한 데이터는 없다. (특강 등록 시 검증했다고 전제함)
  - 특강은 토요일에 진행된다. 토요일이 아닌 날에는 특강이 없다.
  - 특강 신청취소 기능은 제공하지 않는다.
  - 신청이 가능하다는 것이 신청을 완료하지 않았다는 걸 의미하지는 않는다.
    - 신청이 가능하다는 건 (available) 다음을 의미한다.
      - 정원이 다 차지 않은
      - 특강일시가 아직 지나지 않은
    - 신청이 불가능하다는 건 (unavailable) 다음을 의미한다.
      - 정원이 다 찼거나
      - 특강일시가 지났거나
    - 신청을 완료했다는 건 (registered) 다음을 의미한다.
      - 특강 신청 데이터가 있는
    - 신청을 완료하지 않았다는 건 (unregistered) 다음을 의미한다.
      - 특강 신청 데이터가 없는