## EC2 배포
#### 인스턴스 유형: t2.medium
#### 배포 ip: 43.202.60.111

인스턴스 유형의 경우, 프리티어인 t2.micro를 사용하려 했으나 성능이 부족하여 t2.medium 사용.

---
## [Swagger Ui](http://43.202.60.111:8080/swagger-ui/index.html)

---
## Access / Refresh Token 발행과 검증에 관한 테스트 시나리오
#### 1. Access Token을 발행한 username과 검증된 username이 일치하는지 확인
#### 2. Refresh Token을 발행한 username과 검증된 username이 일치하는지 확인
#### 3. 만료된 Token과 만료되지 않은 Token을 구별하는지 확인

---
## AuthController 테스트 시나리오
#### 1. 회원 가입 후 유저 정보가 올바른지 확인
#### 2. 로그인 후 Access / Refresh Token의 유저 정보가 올바른지 확인
#### 3. Access Token 만료 시 Refresh Token 검증 후 재발행 하는지 확인
#### 4. Access Token과 Refresh Token 모두 만료 시 예외 처리
