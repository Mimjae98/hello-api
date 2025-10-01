cd ~/hello-api
cat > README.md <<'EOF'
# hello-api

간단한 **Spring Boot (Java 17)** REST API 예제입니다. `/hello` 엔드포인트를 제공하며 한국 시간과 타임스탬프, 메시지를 JSON으로 반환합니다.

## Tech Stack
- Java 17
- Spring Boot 3.x (`spring-boot-starter-web`)
- Gradle
- (옵션) Docker

---

## 1) 로컬 실행

```bash
./gradlew clean bootRun
curl http://localhost:8080/hello
예상 응답(형식):

json
코드 복사
{
  "koreaTime": "2025-10-01T16:31:44.283072834+09:00[Asia/Seoul]",
  "timestamp": 1759303904283,
  "message": "Hello, World!"
}
2) 도커로 실행 (로컬)
2-1) 이미지 빌드
bash
코드 복사
./gradlew clean bootJar
docker build -t mimjae98/hello-api:0.1 .
2-2) 컨테이너 실행
bash
코드 복사
# 8080 포트 사용
docker run -d --name hello-api -p 8080:8080 mimjae98/hello-api:0.1
curl http://localhost:8080/hello
3) 배포(EC2, 단일 인스턴스)
3-1) Docker Hub 푸시 (로컬)
bash
코드 복사
docker login
docker push mimjae98/hello-api:0.1
3-2) EC2에서 실행
bash
코드 복사
# EC2(ubuntu) 접속
ssh -i ~/keys/aws-min2025.pem ubuntu@13.209.15.173

# Docker 설치 (한 번만)
sudo apt-get update && sudo apt-get install -y docker.io
sudo systemctl enable --now docker
sudo usermod -aG docker $USER && newgrp docker

# 이미지 Pull & 실행 (호스트 80 → 컨테이너 8080)
docker pull docker.io/mimjae98/hello-api:0.1
docker run -d --name hello-api --restart unless-stopped -p 80:8080 docker.io/mimjae98/hello-api:0.1
3-3) 보안 그룹(중요)
EC2 보안 그룹 Inbound에 다음 중 하나를 열어야 외부에서 접근 가능:

HTTP 80/TCP (권장) → 액세스 URL: http://13.209.15.173/hello

또는 TCP 8080 → 액세스 URL: http://13.209.15.173:8080/hello

현재 실습 상태: 8080 포트로 성공 확인됨 → http://13.209.15.173:8080/hello

4) API
GET /hello
Response: 200 OK

Content-Type: application/json

예시:

json
코드 복사
{
  "koreaTime": "2025-10-01T16:31:44.283072834+09:00[Asia/Seoul]",
  "timestamp": 1759303904283,
  "message": "Hello, World!"
}
5) 트러블슈팅
포트 충돌: sudo lsof -i :80 또는 :8080 → 점유 프로세스 종료 후 재시도

컨테이너 로그: docker logs -f hello-api

내부 헬스체크(EC2):

80 사용: curl http://localhost/hello

8080 사용: curl http://localhost:8080/hello

권한 오류(/var/run/docker.sock): sudo usermod -aG docker $USER && newgrp docker
