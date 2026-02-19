# 야 너두 사장 될 수 있어! 🛍️

> 나만의 쇼핑몰 웹사이트 만들기 - Backend Repository

## 📋 프로젝트 소개

멋쟁이사자처럼은 창업 동아리입니다!  
창업에 필수인 홈페이지, 외주 맡기지 말고 직접 만들어봅시다.

이 프로젝트는 실제 운영 가능한 쇼핑몰 웹사이트를 구축하여 창업 실전 역량을 키우고,  
웹 개발의 전반적인 스킬을 학습하는 것을 목표로 합니다.

## 🎨 주요 기능
### 인증 및 인가
- **JWT(JSON Web Token) 기반 무상태 인증 구현**
    - Access Token & Refresh Token 발급 및 검증
    - HS256 알고리즘을 사용한 토큰 서명
    - Refresh Token은 DB에 저장하여 보안성 강화
- **Spring Security를 활용한 역할(Role) 기반 접근 제어**
    - Custom Filter Chain 구성 (JwtValidationFilter, AuthCreationFilter)
    - STATELESS 세션 관리 정책 적용
- **OAuth 2.0 소셜 로그인 연동**
    - 카카오 OAuth 2.0 로그인 구현
    - Provider ID 기반 사용자 식별 및 자동 회원가입

### 회원 관리
- **BCrypt를 이용한 비밀번호 암호화 저장**
    - PasswordEncoder Bean을 통한 안전한 비밀번호 처리
- **사용자 정보 조회 및 수정**
    - 배송지 정보 등록/수정/삭제 (임베디드 타입 활용)
    - 프로필 정보 업데이트

### 주문 및 결제 시스템

- **주문 생성 및 트랜잭션 처리**
    - `@Transactional` 을 통한 원자성 보장
    - 재고 확인 및 총 금액 자동 계산
    - 주문 상태 관리 (PROCESSING → COMPLETE → CANCEL)
- **주문 상태 자동 업데이트**
    - Spring Scheduler(`@Scheduled`)를 활용한 배송 상태 자동 변경
    - 주문 생성 1분 후 자동으로 배송 완료 처리

### 마일리지 시스템

- **주문 완료 시 자동 마일리지 적립**
    - 결제 금액의 10% 자동 적립
    - 도메인 비즈니스 로직으로 캡슐화 (`addMileage()`, `useMileage()`)
- **결제 시 마일리지 사용 및 차감**
    - 사용 가능 마일리지 유효성 검증
    - 최대 사용 가능 금액 제한 (총 금액 초과 불가)
- **주문 취소 시 마일리지 환급 처리**
    - 사용했던 마일리지 자동 환급
    - 적립된 마일리지 회수 로직 구현

### 파일 업로드

- **AWS S3 연동 파일 저장**
    - AmazonS3 SDK를 활용한 객체 스토리지 관리
    - UUID 기반 중복 없는 파일명 생성
    - 업로드된 파일의 Public URL 반환

### 기타 기술 구현

- **Spring Data JPA 활용**
    - Repository 패턴으로 데이터 액세스 계층 구현
    - 엔티티 연관관계 매핑 (OneToMany, OneToOne, Embedded)
- **전역 예외 처리**
    - Custom Exception 및 GlobalExceptionHandler 구현
    - 일관된 API 응답 형식 제공 (ApiResponse, ErrorCode)
- **Swagger API 문서 자동화**
    - SpringDoc OpenAPI를 통한 실시간 API 명세 제공
- **CORS 설정**
    - 프론트엔드 도메인별 접근 권한 관리

## 👥 개발자

| 이름 | GitHub | 역할 |
|------|--------|------|
| 베릴(강민준) | [@MinJunKKang](https://github.com/MinJunKKang) | Backend Developer |
| 쥬쥬(이승주) | [@JuJu-0225](https://github.com/JuJu-0225) | Backend Developer |
| 얀(전유안) | [@yaaan7](https://github.com/yaaan7) | Backend Developer |

## 🛠️ 기술 스택
- Framework : Spring Boot (Java)
- Language : Java
- DB : MySQL
- IDE : IntelliJ
- Deployment/Cloud : AWS

## 📄 라이선스

This project is licensed under the MIT License.

---

**멋쟁이사자처럼 13th at 한국항공대학교** 🦁
