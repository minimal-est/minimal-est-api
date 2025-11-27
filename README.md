# Minimal-est API

블로그 플랫폼을 위한 REST API 백엔드. 사용자가 글을 작성하고, 타인의 글에 댓글을 남기며 반응을 표시할 수 있고, 관심 있는 글을 북마크로 저장하는 기능을 제공합니다.

## 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.5.3
- **데이터베이스**: MySQL, Redis, Elasticsearch
- **ORM**: Spring Data JPA, Hibernate
- **인증**: JWT
- **API 문서**: OpenAPI 3.0 / Swagger UI
- **빌드**: Gradle
- **배포/인프라**: AWS EC2, S3, SES, Lambda, Cloudfront

## 아키텍처

Domain-Driven Design (DDD) 개념을 따릅니다.

```
Web Layer (Controller)
    ↓
Application Layer (Use Cases)
    ↓
Domain Layer (Business Logic)
    ↓
Infrastructure Layer (JPA, Security, S3)
    ↓
Databases..
```

### 도메인 모듈

- **ACCESS**: 사용자 인증/권한 관리
- **PUBLISHING**: 블로그 및 저자 정보
- **WRITING**: 글 작성/발행
- **ENGAGEMENT**: 댓글, 반응
- **DISCOVERY**: 북마크, 컬렉션
