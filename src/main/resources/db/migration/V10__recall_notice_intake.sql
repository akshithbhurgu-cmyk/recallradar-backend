create table recall_notice (
  id uuid primary key,
  organization_id uuid not null,
  submitted_by uuid not null,
  source_type varchar(30) not null,
  file_name varchar(255),
  stored_path varchar(600),
  typed_reference varchar(500),
  status varchar(40) not null,
  confirmed_recall_id uuid references recall_case(id),
  created_at timestamp with time zone not null,
  confirmed_at timestamp with time zone
);
create table recall_notice_fact (
  id uuid primary key,
  notice_id uuid not null references recall_notice(id) on delete cascade,
  field_name varchar(80) not null,
  extracted_value varchar(1000),
  confirmed_value varchar(1000),
  confidence integer not null,
  source_reference varchar(255) not null,
  requires_resolution boolean not null default false,
  created_at timestamp with time zone not null
);
create index idx_recall_notice_organization on recall_notice(organization_id, created_at desc);
create index idx_recall_notice_fact_notice on recall_notice_fact(notice_id);
