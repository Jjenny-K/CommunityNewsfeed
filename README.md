# Community-Newsfeed
사용자가 정보를 공유하고 서로 상호작용 할 수 있는 REST API 구현

## Task interpretation
해당 기능을 사용할 수 있는 권한이 있는 client(게시글 게시자 등)에게 정보 공유 및 사용자 간 상호작용 조회 기능을 제공합니다.

## Index
- [기술 스택](#tech-stack)
- [애플리케이션 아키텍쳐](#architecture)
- [데이터베이스 테이블 구조](#erd)
- [API 명세](#api-specification)
- [구현 과정](#implementation-process)
- [애플리케이션 실행 방법](#step-to-run)

## Implementation

### Tech Stack
<img src="https://img.shields.io/badge/Java-437291?style=flat-square&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>

### Architecture
![architecture drawio](https://github.com/Jjenny-K/CommunityNewsfeed/assets/96185029/ec390014-1af5-40cb-b385-0fa88d0fef41)

### ERD
![erd](https://github.com/Jjenny-K/CommunityNewsfeed/assets/96185029/aa051326-6b2c-434b-938e-9d17dfaf9798)

### API Specification

### Implementation Process

### Step to run
> window 환경에서 구현 및 실행되었습니다.

#### Local server
1. github에서 해당 프로젝트의 repository를 clone합니다.
```shell
$ git clone https://github.com/Jjenny-K/CommunityNewsfeed.git
```

2-1. .env 파일을 root directory에 생성 후, MYSQL, Adminer, Redis와 연동을 위한 정보를 저장합니다.
```
MYSQL_DATABASE='{local database name}'
MYSQL_ROOT_USER='{local database user}'
MYSQL_ROOT_PASSWORD='{local database password}'
LOCAL_DB_PORT='{local database port}'

LOCAL_DB_ADMINER_HOST='{local database adminer host}'

LOCAL_REDIS_HOST_NAME='{local redis host name}'
LOCAL_REDIS_PORT='{local redis port}'
```

2-2. application-local.yml 파일을 classpath에 생성 후, local 프로젝트 환경 정보를 저장합니다.
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:{local database port}/{local database name}
    username: {local database user}
    password: {local database password}
    driver-class-name: com.mysql.cj.jdbc.Driver

  redis:
    host: {local redis host name}
    port: {local redis port}

  servlet:
    multipart:
      enabled: true
      location: "{multipart file stored location}"
      max-request-size: {max request size}  # --MB 형태
      max-file-size: {max file size}  # --MB 형태
  ...

server:
  port: {local server port}
  
jwt:
  header: {jwt header}
  secret: {jwt base secret key}  # Base64 인코딩
  access-token-validity-in-seconds: {access token time}  # 초단위
  refresh-token-validity-in-seconds: {refresh token time}  # 초단위

...
```

2-3. application.yml 파일에 지정한 multipart file stored location에 **profileImages** 이름의 폴더를 생성합니다.

3. docker에 database 이미지를 생성하고 컨테이너화 합니다.
```shell
$ sudo docker-compose up -d
```

4. 프로젝트를 빌드합니다.
```shell
$ ./gradlew build
```

5. local server를 실행합니다.
```shell
$ ./gradlew bootRun --spring.profiles.active=local
```

6. local server를 종료하고 database 컨테이너도 종료합니다.
```shell
$ (ctrl + v) Y
$ sudo docker-compose down
```

## Author
All developments : :monkey_face: **Kang Jeonghui**
