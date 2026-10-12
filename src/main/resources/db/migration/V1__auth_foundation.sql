create table app_user (
 id uuid primary key, full_name varchar(120) not null, email varchar(254) not null unique,
 password_hash varchar(100) not null, role varchar(40) not null, enabled boolean not null default true,
 created_at timestamp with time zone not null
);
create table refresh_session (
 id uuid primary key, user_id uuid not null references app_user(id) on delete cascade,
 token_hash varchar(64) not null unique, expires_at timestamp with time zone not null,
 revoked_at timestamp with time zone, created_at timestamp with time zone not null
);
create table password_reset_token (
 id uuid primary key, user_id uuid not null references app_user(id) on delete cascade,
 token_hash varchar(64) not null unique, expires_at timestamp with time zone not null,
 used_at timestamp with time zone, created_at timestamp with time zone not null
);
