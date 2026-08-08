CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMPTZ
);

CREATE TABLE user_preference (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    is_daily_push_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_preference_user UNIQUE (user_id),
    CONSTRAINT fk_user_preference_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE emergency_contact (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_emergency_contact_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE user_auth_provider (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    provider_type VARCHAR(255) NOT NULL,
    provider_account_id VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    is_primary BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_auth_provider_type
        CHECK (provider_type IN ('EMAIL', 'GOOGLE', 'APPLE')),
    CONSTRAINT ck_auth_provider_password
        CHECK (
            (provider_type = 'EMAIL' AND password_hash IS NOT NULL)
            OR (provider_type <> 'EMAIL' AND password_hash IS NULL)
        ),
    CONSTRAINT uk_auth_provider_account
        UNIQUE (provider_type, provider_account_id),
    CONSTRAINT uk_auth_provider_user_type
        UNIQUE (user_id, provider_type),
    CONSTRAINT fk_auth_provider_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE UNIQUE INDEX uk_auth_provider_primary_user
    ON user_auth_provider (user_id)
    WHERE is_primary = TRUE;

CREATE TABLE app_lock_credential (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    pin_hash VARCHAR(255) NOT NULL,
    failed_attempt_count INTEGER NOT NULL DEFAULT 0,
    locked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_app_lock_credential_user UNIQUE (user_id),
    CONSTRAINT ck_app_lock_failed_attempt_count
        CHECK (failed_attempt_count >= 0),
    CONSTRAINT fk_app_lock_credential_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE authentication_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    action VARCHAR(255) NOT NULL,
    provider_type VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_authentication_history_action
        CHECK (action IN (
            'LOGIN_SUCCEEDED',
            'LOGIN_FAILED',
            'AUTH_PROVIDER_LINKED',
            'AUTH_PROVIDER_UNLINKED',
            'PRIMARY_PROVIDER_CHANGED',
            'PASSWORD_CHANGED',
            'PIN_VERIFICATION_FAILED',
            'APP_LOCKED',
            'APP_UNLOCKED',
            'PIN_CHANGED'
        )),
    CONSTRAINT ck_authentication_history_provider_type
        CHECK (provider_type IS NULL OR provider_type IN ('EMAIL', 'GOOGLE', 'APPLE'))
);

CREATE TABLE greeting (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    time_period VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_greeting_time_period
        CHECK (time_period IN ('MORNING', 'AFTERNOON', 'EVENING', 'DAWN'))
);

CREATE TABLE daily_habit_check (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    check_date DATE NOT NULL,
    habit_type VARCHAR(255) NOT NULL,
    checked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_daily_habit_type
        CHECK (habit_type IN (
            'ADEQUATE_SLEEP',
            'ADEQUATE_MEALS',
            'GOOD_HYGIENE',
            'SUFFICIENT_FOCUS',
            'HEALTHY_RELATIONSHIPS',
            'PHYSICAL_ACTIVITY',
            'UNSPECIFIED_1',
            'UNSPECIFIED_2',
            'UNSPECIFIED_3'
        )),
    CONSTRAINT uk_daily_habit_check_user_date_type
        UNIQUE (user_id, check_date, habit_type),
    CONSTRAINT fk_daily_habit_check_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE medication_intake_check (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    intake_period VARCHAR(255) NOT NULL,
    intake_date DATE NOT NULL,
    checked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_medication_intake_period
        CHECK (intake_period IN ('MORNING', 'LUNCH', 'EVENING', 'BEDTIME', 'AS_NEEDED')),
    CONSTRAINT uk_medication_user_intake_period_date
        UNIQUE (user_id, intake_period, intake_date),
    CONSTRAINT fk_medication_intake_check_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE medication_intake_check_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    intake_period VARCHAR(255) NOT NULL,
    intake_date DATE NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_medication_history_intake_period
        CHECK (intake_period IN ('MORNING', 'LUNCH', 'EVENING', 'BEDTIME', 'AS_NEEDED')),
    CONSTRAINT ck_medication_history_type
        CHECK (type IN ('CHECK', 'UNCHECK'))
);

CREATE TABLE menstrual_cycle_profile (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    average_cycle_length_days INTEGER NOT NULL,
    average_period_duration_days INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_menstrual_cycle_profile_user UNIQUE (user_id),
    CONSTRAINT ck_menstrual_cycle_profile_lengths
        CHECK (
            average_cycle_length_days > 1
            AND average_period_duration_days > 0
            AND average_period_duration_days < average_cycle_length_days
        ),
    CONSTRAINT fk_menstrual_cycle_profile_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE menstrual_period (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    start_date DATE NOT NULL,
    duration_days INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_menstrual_period_duration
        CHECK (duration_days > 0),
    CONSTRAINT uk_period_user_start_date
        UNIQUE (user_id, start_date),
    CONSTRAINT fk_menstrual_period_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE psychiatric_visit (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    visit_date DATE NOT NULL,
    visit_time TIME NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_psy_user_visit_date_time
        UNIQUE (user_id, visit_date, visit_time),
    CONSTRAINT fk_psychiatric_visit_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE bingo_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content VARCHAR(255) NOT NULL,
    is_highlighted BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_bingo_item_content UNIQUE (content)
);

CREATE TABLE daily_bingo_board (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    board_date DATE NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    reroll_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_daily_bingo_board_reroll_count
        CHECK (reroll_count >= 0),
    CONSTRAINT uk_daily_bingo_board_user_board_date
        UNIQUE (user_id, board_date),
    CONSTRAINT fk_daily_bingo_board_user
        FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE daily_bingo_check (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    daily_bingo_board_id UUID NOT NULL,
    bingo_item_id UUID NOT NULL,
    content_snapshot VARCHAR(255) NOT NULL,
    is_highlighted_snapshot BOOLEAN NOT NULL,
    cell_position INTEGER NOT NULL,
    is_replacement BOOLEAN NOT NULL DEFAULT FALSE,
    is_checked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_daily_bingo_check_cell_position
        CHECK (cell_position BETWEEN 0 AND 8),
    CONSTRAINT uk_daily_bingo_check_board_item
        UNIQUE (daily_bingo_board_id, bingo_item_id),
    CONSTRAINT uk_daily_bingo_check_board_item_content
        UNIQUE (daily_bingo_board_id, content_snapshot),
    CONSTRAINT uk_daily_bingo_board_cell_position
        UNIQUE (daily_bingo_board_id, cell_position),
    CONSTRAINT fk_daily_bingo_check_board
        FOREIGN KEY (daily_bingo_board_id) REFERENCES daily_bingo_board (id),
    CONSTRAINT fk_daily_bingo_check_item
        FOREIGN KEY (bingo_item_id) REFERENCES bingo_item (id)
);

CREATE TABLE bingo_item_replacement_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    daily_bingo_board_id UUID NOT NULL,
    cell_position INTEGER NOT NULL,
    previous_bingo_item_id UUID NOT NULL,
    previous_content VARCHAR(255) NOT NULL,
    previous_is_highlighted BOOLEAN NOT NULL,
    replacement_bingo_item_id UUID NOT NULL,
    replacement_content VARCHAR(255) NOT NULL,
    replacement_is_highlighted BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_bingo_replacement_history_cell_position
        CHECK (cell_position BETWEEN 0 AND 8)
);

CREATE INDEX ix_emergency_contact_user
    ON emergency_contact (user_id);

CREATE INDEX ix_authentication_history_user_created_at
    ON authentication_history (user_id, created_at DESC);

CREATE INDEX ix_medication_history_user_date
    ON medication_intake_check_history (user_id, intake_date DESC);

CREATE INDEX ix_bingo_replacement_history_board_created_at
    ON bingo_item_replacement_history (daily_bingo_board_id, created_at DESC);
