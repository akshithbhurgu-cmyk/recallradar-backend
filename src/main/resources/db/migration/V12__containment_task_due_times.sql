alter table recall_containment_task add column due_at timestamp with time zone;
update recall_containment_task set due_at = created_at + interval '4 hours' where due_at is null;
alter table recall_containment_task alter column due_at set not null;
