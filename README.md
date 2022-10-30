# Numble 9팀 백엔드

## Git Commit Convention

|Type| 설명                                   |
|---|--------------------------------------|
|feat| 새로운 타입 추가                            |
|fix| 버그 수정                                
|docs| 문서 수정                                |
|style| 코드 포맷팅, 세미콜론 누락 수정 등 코드 자체 변경이 없는 경우 |
|refactor| 로직의 변동이 없는 한에서의 코드 리펙토링              |
|test| 테스크 관련 코드                            |
|build| 빌드 관련 파일 수정                          |
|ci| ci 설정 관련 파일 수정                       |
|perf|성능 개선|

```shell
ex) feat: #issue 회원가입 기능 추가
```

## Git Branch 전략

![](https://velog.velcdn.com/images/appti/post/4f339223-57ee-403f-9a02-7f84092fd6af/image.png)

`Github-flow` 전략 사용

- 새로운 기능은 `main` 브랜치를 기준으로 별도 브랜치(`feat`)를 생성해서 작업 진행
  - 브랜치는 로컬에서 `commit`하며, 기능 개발이 완료된 경우 원격 브랜치에 `push`
  - `merge`시 반드시 `Pull Request`를 작성해 코드 리뷰를 진행한 뒤 진행
- `main` 브랜치의 경우 `Github Action`을 통해 항상 자동으로 배포