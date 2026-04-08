# AGENTS.md

## 프로젝트 목적

이 저장소는 **개인 소유 NFC 태그의 공개 메타데이터만 분석하는 안드로이드 앱**을 유지보수하기 위한 저장소입니다.

## 빠른 시작

```bash
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
```

## 설치 / 실행 / 테스트 명령

- 빌드: `./gradlew assembleDebug`
- 단위 테스트: `./gradlew testDebugUnitTest`
- 린트: `./gradlew lintDebug`

## 기본 작업 순서

1. 저장소 현실 파악
2. 변경 범위 최소화
3. 안전 경계 유지 여부 확인
4. 코드 수정
5. 테스트 추가 또는 수정
6. README / docs 갱신
7. 빌드와 테스트 검증

## 완료 조건

- 공개 메타데이터 전용 분석 범위를 유지한다.
- 새 코드가 빌드된다.
- 관련 테스트가 존재하고 실행된다.
- README, AGENTS, docs가 최신 상태다.

## 코드 스타일 원칙

- Kotlin 공식 스타일 사용
- Compose UI는 상태 기반으로 작성
- NFC 접근은 `feature/nfc/` 아래에만 둔다.
- 플랫폼 의존 코드와 포맷팅/보고서 로직을 분리한다.

## 파일 구조 원칙

- `core/`: 포맷팅, 모델, 보고서 생성
- `data/`: Room, DataStore, 저장소
- `feature/`: 화면 및 ViewModel
- `navigation/`: Compose Navigation
- `ui/`: Theme

## 문서화 원칙

- 사용자 문서는 한국어 기준으로 유지
- 안전 경계를 변경하면 반드시 문서를 갱신
- 기능 추가 시 `docs/changes/` 문서 추가

## 테스트 원칙

- 포맷팅, 보고서, 경고 필드 같은 안전 핵심 로직은 단위 테스트 우선
- NFC 하드웨어 의존 동작은 가능한 범위에서 분리하고 수동 검증 계획을 남긴다.

## 브랜치 / 커밋 / PR 규칙

- 기본 브랜치에서 직접 작업하지 않음
- 권장 브랜치 예시: `feat/public-nfc-analyzer`
- 권장 커밋 형식: `feat(scope): ...`, `test(scope): ...`, `docs(scope): ...`

## 민감한 경로 / 수정 주의 경로

- `feature/nfc/`: 공개 메타데이터 범위를 넘기지 않도록 주의
- `AndroidManifest.xml`: NFC intent filter와 provider 설정 유지
- `.github/workflows/`: CI 신뢰성에 직접 영향

## 작업 전 체크리스트

- 현재 요구사항이 공개 메타데이터 분석 범위인지 확인했는가?
- 인증, 덤프, 복제 관련 기능이 요구사항에 숨어 있지 않은가?
- 기존 README / docs와 충돌하지 않는가?

## 작업 후 체크리스트

- 금지 API가 추가되지 않았는가?
- 빌드와 테스트를 실제로 실행했는가?
- README / AGENTS / docs를 업데이트했는가?

## 절대 하면 안 되는 것

- `MifareClassic.connect()` 호출
- `authenticateSectorWithKeyA/B()` 호출
- `transceive()` 기반 저수준 프로빙 추가
- 섹터/블록 읽기 및 덤프 기능 추가
- 카드 쓰기 또는 복제 관련 코드 추가
