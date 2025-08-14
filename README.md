# Music Generator

Loudy API를 활용한 AI 음악 생성 Android 애플리케이션

## 주요 기능
- AI 기반 음악 생성 (프롬프트 입력)
- 생성된 음악 저장 및 관리
- 즐겨찾기 기능
- 일일 생성 한도 관리

## 시작하기

### 요구사항
- Android Studio Hedgehog 이상
- JDK 17
- Android SDK 34
- Kotlin 2.2.0

### API 키 설정
프로젝트 루트의 `local.properties` 파일에 Loudy API 키를 추가하세요:
```properties
loudy.api.key=your_api_key_here
loudy.base.url=https://api.loudy.com/
```

### 빌드 및 실행
```bash
# 프로젝트 클론
git clone https://github.com/yourusername/music-generator.git

# 프로젝트 빌드
./gradlew build

# 앱 실행
./gradlew installDebug
```

## 아키텍처

### Clean Architecture 원칙

이 프로젝트는 **Clean Architecture**를 기반으로 구현되었으며, Google의 [앱 아키텍처 가이드](https://developer.android.com/topic/architecture)를 참고하여 설계되었습니다.

#### 레이어 구조

```
┌─────────────────────────────────────────────┐
│            Presentation Layer               │
│                  (app)                      │
├─────────────────────────────────────────────┤
│              Domain Layer                   │
│            (core:domain)                    │
├─────────────────────────────────────────────┤
│               Data Layer                    │
│             (core:data)                     │
├─────────────────────────────────────────────┤
│           Data Source Layer                 │
│    (core:network, core:database)            │
└─────────────────────────────────────────────┘
```

### 모듈별 역할 및 컨벤션

#### 1. Domain Layer (`:core:domain`)

**역할:**
- 비즈니스 로직의 정의
- 앱의 핵심 기능과 규칙을 캡슐화
- 플랫폼 독립적인 순수 Kotlin 모듈

**구성 요소:**
- **Model**: 비즈니스 엔티티 (예: `Song`, `FavoriteSong`, `AccountLimitInfo`)
- **Repository Interface**: 데이터 접근 추상화
- **UseCase**: 단일 비즈니스 로직 실행

**컨벤션:**
- Android 의존성을 가지지 않음 (Pure Kotlin)
- 모든 Repository는 인터페이스로 정의
- UseCase는 단일 책임 원칙을 따름
- 각 UseCase는 하나의 invoke() 연산자 함수만 가짐
- 비즈니스 규칙과 검증 로직은 도메인 모델 내부에 구현


#### 2. Data Layer (`:core:data`)

**역할:**
- Repository 구현체 제공
- 데이터 소스 조율 및 캐싱 전략 구현
- 도메인 모델과 DTO 간 매핑

**구성 요소:**
- **Repository Implementation**: 도메인 레이어의 Repository 인터페이스 구현
- **DataSource Interface**: 로컬/원격 데이터 소스 추상화
- **DTO (Data Transfer Object)**: 데이터 소스와 통신용 모델
- **Mapper**: DTO ↔ Domain Model 변환

**컨벤션:**
- Repository 구현체는 `~RepositoryImpl` 네이밍
- 데이터 소스 선택 로직은 Repository에서 처리
- DTO는 `~Dto` 접미사 사용
- 매핑 함수는 확장 함수로 구현 (`toModel()`, `toDto()`)

#### 3. Data Source Layer

##### Network Module (`:core:network`)
- Retrofit을 사용한 API 통신
- API Response 모델 정의
- 네트워크 에러 처리

##### Database Module (`:core:database`)
- Room 데이터베이스 구현
- Entity 정의 및 DAO 구현
- 로컬 캐싱 전략

#### 4. Presentation Layer (`:app`, `:feature:*`)

**역할:**
- UI 구현 (Jetpack Compose)
- ViewModel을 통한 상태 관리
- 사용자 상호작용 처리

**컨벤션:**
- MVI (Model-View-Intent) 패턴 사용
- UI State는 data class로 정의 (단일 상태 객체)
- Intent는 sealed interface로 정의 (사용자 액션)
- Side Effect는 sealed interface로 관리 (일회성 이벤트)

### 의존성 규칙

#### 의존성 방향
```
app → domain
app → data → domain
app → network → data → domain
app → database → data → domain
```

#### 모듈 의존성 그래프
```
:app
 ├── :core:domain
 ├── :core:data
 │   └── :core:domain
 ├── :core:network
 │   ├── :core:data
 │   └── :core:domain
 └── :core:database
     ├── :core:data
     └── :core:domain
```

#### 핵심 원칙

1. **의존성 역전 원칙 (DIP)**
   - 상위 레이어는 하위 레이어의 구현체가 아닌 인터페이스에 의존
   - Domain 레이어는 어떤 레이어에도 의존하지 않음

2. **단방향 의존성**
   - 의존성은 항상 안쪽(Domain)을 향함
   - 순환 의존성 금지

3. **레이어 격리**
   - 각 레이어는 자신의 책임에만 집중
   - 레이어 간 통신은 정의된 인터페이스를 통해서만 수행

### 테스트 전략

- **Domain Layer**: 비즈니스 로직 단위 테스트
- **Data Layer**: Repository 테스트 (Fake DataSource 사용)
- **Presentation Layer**: ViewModel 테스트, UI 테스트

### 기술 스택

- **Architecture**: Clean Architecture + MVI
- **DI**: Hilt
- **Async**: Coroutines + Flow
- **UI**: Jetpack Compose
- **Network**: Retrofit + OkHttp
- **Database**: Room
- **Serialization**: Kotlinx Serialization
- **Test Coverage**: Jacoco
