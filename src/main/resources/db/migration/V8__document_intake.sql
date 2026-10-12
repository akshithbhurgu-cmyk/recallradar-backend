create table intake_job (
  id uuid primary key,
  organization_id uuid not null,
  submitted_by uuid not null,
  source_type varchar(30) not null,
  file_name varchar(255) not null,
  status varchar(30) not null,
  created_at timestamp with time zone not null
);
create table intake_line (
  id uuid primary key,
  job_id uuid not null references intake_job(id) on delete cascade,
  line_number integer not null,
  ingredient_name varchar(160) not null,
  supplier_name varchar(160) not null,
  supplier_batch_number varchar(120) not null,
  expiry_date date,
  quantity numeric(19,3) not null,
  unit varchar(24) not null,
  location_name varchar(160) not null,
  status varchar(30) not null,
  issue varchar(500),
  receipt_lot_id uuid,
  created_at timestamp with time zone not null
);
create index idx_intake_job_organization on intake_job(organization_id, created_at desc);
create index idx_intake_line_job on intake_line(job_id, line_number);
