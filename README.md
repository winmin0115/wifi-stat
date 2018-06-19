통계 조회 어플리케이션


Build Setup
# Import Project
- Eclipse 또는 SpringToolSuite(STS) IDE를 실행
- FIle -> Import -> archive file -> wifi-stat.zip 선택


# DB 및 서버 포트 세팅
프로젝트 내 src/main/resources => application.properties에서 DB connection 설정 및 server.port 설정

# RUN
프로젝트 선택 => 우클릭 => Spring Boot App 선택 또는 StatServerApplication.java 선택 후 우클릭 => java application 선택

# TEST
localhost:{{port}}/swagger-ui.html에 접속하여 TEST 진행

# 로직
일별 평균 체류 시간 조회
1. 10시부터 23시 사이에 한번 이상 AP에 접속한 유니크한 MAC 주소 조회
2. 해당 MAC 주소들을 기반으로 체류 시간을 구함.
3. 각 각의 계산 된 체류 시간들을 통해서 평균 체류 시간을 계산함.

체류 시간대별 방문자 수
1. 10시부터 23시 사이에 한번 이상 AP에 접속한 유니크한 MAC 주소 조회
2. 해당 MAC 주소들을 기반으로 체류 시간을 구함.
3. 구해진 체류 시간이 30분 이하, 1시간 이하, 2시간 이하, 3시간 미만, 3시간 이상일 경우 방문자  계산.

층별 평균 체류 시간
1. 10시부터 23시 사이에 해당 층 AP에 접속 된 MAC 주소 조회
2. 해당 MAC 주소들을 기반으로 체류 시간을 구함.
3. 각 각의 계산 된 체류 시간들을 통해서 평균 체류 시간을 계산함.
