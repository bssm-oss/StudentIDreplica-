# 학생증 레플리카 (분석기)

`학생증 레플리카 (분석기)`는 **개인 소유 NFC 태그의 공개 메타데이터만 확인하는 교육용/진단용 안드로이드 앱**입니다. 이 저장소의 구현 범위는 `UID`, `기술 목록(tech list)`, `ATQA`, `SAK`처럼 **태그가 발견 시점에 공개하는 정보**에만 한정됩니다.

이 앱은 **복사 도구가 아닙니다.** 인증 키 추측, 섹터 읽기, 데이터 덤프 생성, 복제 카드 쓰기 기능은 의도적으로 포함하지 않았습니다.

## 핵심 기능

- 최초 실행 시 비활성화 불가능한 법적 경고 다이얼로그 표시
- `NfcA` 기반 공개 메타데이터 읽기
- 최근 분석 결과 표시
- Room DB 기반 기록 저장 및 상세 화면 조회
- 공개 메타데이터 전용 텍스트 보고서 공유
- 저장되는 JSON 파일 상단에 공개 메타데이터 전용 경고 자동 삽입

## 안전 경계

이 프로젝트는 아래 기능을 **절대 구현하지 않습니다.**

- MIFARE Classic 인증 키 시도
- `MifareClassic.connect()` 호출
- `authenticateSectorWithKeyA()` / `authenticateSectorWithKeyB()` 호출
- 섹터/블록 읽기
- `.mct` 덤프 생성
- UID 변경 가능 카드 쓰기
- 복제 목적 기능

## 기술 스택

- Kotlin
- Jetpack Compose (Material 3)
- MVVM + StateFlow
- Room Database
- Hilt
- Kotlin Coroutines / Flow
- GitHub Actions

## 요구 환경

- Android Studio 최신 안정 버전 권장
- JDK 17
- Android SDK / Build Tools
- NFC 지원 안드로이드 기기 1대

## 빠른 설치 방법

### 1) 저장소 클론

```bash
git clone https://github.com/bssm-oss/StudentIDreplica-.git
cd StudentIDreplica-
```

### 2) Android Studio로 열기

- Android Studio 실행
- `Open` 선택
- 이 저장소 루트 폴더 선택
- Gradle Sync 완료 대기

### 3) 기기에서 NFC 켜기

- 안드로이드 설정 → 연결/네트워크 → NFC 활성화

### 4) 앱 실행

```bash
./gradlew assembleDebug
```

생성된 APK는 `app/build/outputs/apk/debug/` 아래에 생성됩니다.

## 로컬 개발 명령

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

## 앱 사용 방법

1. 앱을 처음 실행하면 법적 경고가 표시됩니다.
2. `동의함`을 누르기 전까지 모든 기능은 비활성화됩니다.
3. `태그 읽기` 버튼을 누릅니다.
4. 개인 소유 태그를 기기 뒷면에 가까이 댑니다.
5. 공개 메타데이터가 읽히면 홈 화면과 기록 목록에 저장됩니다.
6. 기록 상세 화면에서 텍스트 보고서를 공유할 수 있습니다.

## 폴더 구조

```text
.
├── app/
│   ├── src/main/java/org/bssm/studentidreplica/
│   │   ├── core/
│   │   ├── data/
│   │   ├── di/
│   │   ├── feature/
│   │   ├── navigation/
│   │   └── ui/
│   └── src/test/
├── docs/
├── gradle/
└── .github/workflows/
```

## 아키텍처 개요

- `MainActivity`는 NFC 인텐트를 받아 `HomeViewModel`에 전달합니다.
- `NfcReaderHelper`는 `Tag.id`, `Tag.techList`, `NfcA.atqa`, `NfcA.sak`만 읽습니다.
- `RoomTagRepository`는 Room DB 저장과 JSON 아카이브 파일 작성을 담당합니다.
- Compose 화면은 `Home`, `HistoryList`, `Detail` 세 흐름으로 구성됩니다.

## 테스트

현재 저장소에는 다음 단위 테스트가 포함됩니다.

- UID/ATQA 포맷팅 테스트
- 태그 유형 설명 테스트
- JSON 경고 필드 테스트
- 공유 보고서 문자열 테스트

## CI

GitHub Actions에서 아래 작업을 실행합니다.

- `lintDebug`
- `testDebugUnitTest`
- `assembleDebug`

## 알려진 제한 사항

- 이 앱은 공개 메타데이터만 읽기 때문에 카드 내부 사용자 데이터는 분석하지 않습니다.
- 실제 NFC 스캔 수동 검증에는 NFC 지원 물리 기기가 필요합니다.
- 현재 메모(`label`) 입력 UI는 제공하지 않으며, 필드는 후속 확장을 위해 남겨두었습니다.

## 기여 원칙

- 공개 메타데이터만 다루는 현재 안전 경계를 유지해야 합니다.
- 인증/덤프/복제 기능 제안은 수용하지 않습니다.
- 모든 사용자 문구는 한국어 기준으로 유지합니다.

## 향후 계획

- 기록 검색 및 필터링
- JSON 파일 공유 UI 추가
- 계측 테스트 및 스크린샷 문서 보강
