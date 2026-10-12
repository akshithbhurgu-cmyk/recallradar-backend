create table team_invitation (
 id uuid primary key,
 organization_id uuid not null references organization(id) on delete cascade,
 email varchar(254) not null,
 role varchar(40) not null,
 token_hash varchar(128) not null unique,
 expires_at timestamp with time zone not null,
 accepted_at timestamp with time zone,
 created_at timestamp with time zone not null
);
create index idx_team_invitation_org on team_invitation(organization_id);
