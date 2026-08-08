# syntax=docker/dockerfile:1.7

FROM eclipse-temurin:25-jdk AS builder

WORKDIR /workspace

# 빌드 설정이 바뀌지 않으면 Gradle Wrapper와 의존성 계층을 재사용한다.
COPY gradlew gradlew
COPY gradle gradle
COPY settings.gradle build.gradle.kts ./

RUN chmod +x gradlew
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

# 애플리케이션 소스 수정은 이 계층부터 빌드를 다시 수행한다.
COPY src/main src/main

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon -x test --build-cache

FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

RUN groupadd --system spring \
    && useradd --system --gid spring spring

COPY --from=builder --chown=spring:spring /workspace/build/libs/*.jar app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
