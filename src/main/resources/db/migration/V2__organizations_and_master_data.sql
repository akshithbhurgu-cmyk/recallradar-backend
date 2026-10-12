create table organization (
 id uuid primary key, name varchar(160) not null, slug varchar(180) not null unique, created_at timestamp with time zone not null
);
create table org_membership (
 id uuid primary key, user_id uuid not null references app_user(id) on delete cascade,
 organization_id uuid not null references organization(id) on delete cascade,
 role varchar(40) not null, created_at timestamp with time zone not null,
 unique(user_id, organization_id)
);
create table location (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 name varchar(160) not null, type varchar(30) not null, address varchar(500), active boolean not null default true, created_at timestamp with time zone not null,
 unique(organization_id, name)
);
create table supplier (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 name varchar(180) not null, contact_name varchar(120), email varchar(254), phone varchar(40), active boolean not null default true, created_at timestamp with time zone not null,
 unique(organization_id, name)
);
create table ingredient (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 name varchar(180) not null, category varchar(100), unit varchar(24) not null, active boolean not null default true, created_at timestamp with time zone not null,
 unique(organization_id, name)
);
create table supplier_ingredient (
 supplier_id uuid not null references supplier(id) on delete cascade,
 ingredient_id uuid not null references ingredient(id) on delete cascade,
 supplier_sku varchar(100), primary key(supplier_id, ingredient_id)
);
create index idx_membership_user on org_membership(user_id);
create index idx_location_org on location(organization_id);
create index idx_supplier_org on supplier(organization_id);
create index idx_ingredient_org on ingredient(organization_id);
