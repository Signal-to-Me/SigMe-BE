plugins {
	kotlin("jvm") version "2.3.21"
	kotlin("plugin.spring") version "2.3.21"    // open 키워드 자동 추가
	kotlin("plugin.jpa") version "2.3.21"       // no-arg 생성자 자동 생성
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
}

// Spring AI를 다시 사용할 때 BOM 버전을 하나로 관리
// val springAiVersion by extra("2.0.0")

group = "com.sigme"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring MVC 기반 웹 API와 Kotlin JSON 직렬화
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("tools.jackson.module:jackson-module-kotlin")

	// JPA 영속성과 관계형 데이터베이스 연동
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("com.h2database:h2")

	// 인증·인가와 애플리케이션 보안
	implementation("org.springframework.boot:spring-boot-starter-security")

	// JWT 발급·검증
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

	// Spring AI 모델 제공자 연동 (현재 미사용)
	// implementation("org.springframework.ai:spring-ai-starter-model-deepseek")
	// implementation("org.springframework.ai:spring-ai-starter-model-openai")

	// Spring이 Kotlin 타입과 리플렉션 정보를 처리하는 데 사용
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// 통합 테스트와 Spring Security 테스트 지원
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")

	// Kotest 테스트 엔진과 기본 단언문 지원
	testImplementation(platform("io.kotest:kotest-bom:6.0.3"))
	testImplementation("io.kotest:kotest-runner-junit5")
	testImplementation("io.kotest:kotest-assertions-core")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.named<Test>("test") {
	useJUnitPlatform()
}
dependencyManagement {
	imports {
		// Spring AI를 다시 사용할 때 스타터의 릴리스 조합을 BOM으로 통일
		// mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
	}
}
