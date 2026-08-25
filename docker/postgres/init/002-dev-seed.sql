-- 개발 단계 전용 시드 데이터.
-- 001-schema.sql 이 만든 스키마 위에 로그인 확인용 계정을 넣는다.
--
-- 주의
--   * 운영 환경에 올리지 않는다. 비밀번호가 저장소에 그대로 적혀 있다.
--   * docker-entrypoint-initdb.d 는 볼륨이 비어 있을 때만 실행된다.
--     이미 데이터가 있으면 `docker compose down -v` 로 볼륨을 지우고 다시 올린다.
--
-- 모든 계정의 비밀번호는 password1234 이다.
-- password_hash 는 PasswordEncoderFactories.createDelegatingPasswordEncoder() 로
-- 생성한 실제 값이며 {bcrypt} 접두사를 포함한다.

-- 식별자를 고정해 두면 테스트와 수동 확인에서 그대로 참조할 수 있다.
INSERT INTO app_user (id, user_name, deleted_at) VALUES
    ('00000000-0000-0000-0000-000000000001', '테스트유저', NULL),
    ('00000000-0000-0000-0000-000000000002', '탈퇴유저',   '2026-01-01T00:00:00Z'),
    ('00000000-0000-0000-0000-000000000003', '소셜유저',   NULL)
ON CONFLICT (id) DO NOTHING;

-- provider_account_id 는 UserAuthProvider.emailNormalize 를 거친 형태와 같아야 한다.
-- 소문자이고 앞뒤 공백이 없어야 조회에 걸린다.
INSERT INTO user_auth_provider (id, user_id, provider_type, provider_account_id, password_hash, is_primary) VALUES
    (
        '00000000-0000-0000-0000-0000000000a1',
        '00000000-0000-0000-0000-000000000001',
        'EMAIL',
        'test@sigme.com',
        '{bcrypt}$2a$10$9zIX5a1GFG.irXr5kjEOxuWqsPaQhen45.ndFTltRWpneSLOwmk/6',
        TRUE
    ),
    (
        '00000000-0000-0000-0000-0000000000a2',
        '00000000-0000-0000-0000-000000000002',
        'EMAIL',
        'deleted@sigme.com',
        '{bcrypt}$2a$10$9zIX5a1GFG.irXr5kjEOxuWqsPaQhen45.ndFTltRWpneSLOwmk/6',
        TRUE
    ),
    (
        '00000000-0000-0000-0000-0000000000a3',
        '00000000-0000-0000-0000-000000000003',
        'GOOGLE',
        'google-subject-000000000003',
        NULL,
        TRUE
    )
ON CONFLICT (id) DO NOTHING;
