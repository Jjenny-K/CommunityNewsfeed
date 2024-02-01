# Pre-order-Purchase
사용자가 정보를 공유하고 관련 상품을 예약 거래하는 REST API 구현

## Task interpretation
해당 기능을 사용할 수 있는 권한이 있는 client(게시글 게시자, 거래 서비스 사용자 등)에게 정보 공유 및 상품 예약 거래 기능을 제공하는 서비스로 해석하였습니다.

## Implementation

### Tech Stack
<img src="https://img.shields.io/badge/Java-437291?style=flat-square&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>

### Step to run
> window 환경에서 구현 및 실행되었습니다.

#### Local server
1. github에서 해당 프로젝트의 repository를 clone합니다.
```shell
$ git clone https://github.com/Jjenny-K/PreOrderPurchase.git
```

2-1. .env 파일을 root directory에 생성 후, MYSQL, Redis와 연동을 위한 정보를 저장합니다.
```
MYSQL_DATABASE='{local database name}'
MYSQL_ROOT_USER='{local database user}'
MYSQL_ROOT_PASSWORD='{local database password}'
LOCAL_DB_PORT='{local database port}'

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
