alter table recall_case add column containment_incident_id uuid references incident(id);

create table recall_containment_task (
  id uuid primary key,
  recall_case_id uuid not null references recall_case(id) on delete cascade,
  task_type varchar(60) not null,
  description varchar(500) not null,
  completed boolean not null default false,
  created_at timestamp with time zone not null,
  completed_at timestamp with time zone,
  completed_by uuid references app_user(id)
);

create index idx_recall_containment_task_case on recall_containment_task(recall_case_id, created_at);
