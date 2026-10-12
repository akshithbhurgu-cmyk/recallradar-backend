alter table inventory_lot add column version bigint not null default 0;
create table inventory_balance (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 lot_id uuid not null references inventory_lot(id) on delete cascade,
 location_id uuid not null references location(id), quantity numeric(19,3) not null,
 version bigint not null default 0, updated_at timestamp with time zone not null,
 unique(lot_id, location_id)
);
create index idx_inventory_balance_org on inventory_balance(organization_id);
create table inventory_command (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 actor_id uuid not null references app_user(id), idempotency_key varchar(120) not null,
 operation varchar(40) not null, lot_id uuid references inventory_lot(id), created_at timestamp with time zone not null,
 unique(organization_id, actor_id, idempotency_key)
);
create table audit_event (
 id uuid primary key, organization_id uuid references organization(id) on delete cascade,
 actor_id uuid references app_user(id), action varchar(80) not null, entity_type varchar(80) not null,
 entity_id uuid, details varchar(1000), occurred_at timestamp with time zone not null
);
create index idx_audit_event_org_time on audit_event(organization_id, occurred_at);
