# Handicraft
핸드메이드 취미를 공유하고 관련 상품을 예약 거래하는 REST API 구현

## Task interpretation
해당 기능을 사용할 수 있는 권한이 있는 client(게시글 게시자, 거래 서비스 사용자 등)에게 취미 공유 및 상품 예약 거래 기능을 제공하는 서비스로 해석하였습니다.

## Implementation

### Tech Stack
<img src="https://img.shields.io/badge/Java-437291?style=flat-square&logo=OpenJDK&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/IntelliJ-000000?style=flat-square&logo=IntelliJ IDEA&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white"/>

### Step to run
> window 환경에서 구현 및 실행되었습니다.

#### Local server
1. github에서 해당 프로젝트의 repository를 clone합니다.
```shell
$ git clone https://github.com/Jjenny-K/handicraft.git
```

2. .env 파일을 root directory에 생성 후, MYSQL과 연동을 위한 정보를 저장합니다.
```
MYSQL_DATABASE='{local database name}'
MYSQL_ROOT_USER='{local database user}'
MYSQL_ROOT_PASSWORD='{local database password}'
LOCAL_DB_HOST='{local database host}'
```

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
$ ./gradlew bootRun
```

6. local server를 종료하고 database 컨테이너도 종료합니다.
```shell
$ (ctrl + v) y
$ sudo docker-compose down
```

## Author
All developments : :monkey_face: **Kang Jeonghui**
