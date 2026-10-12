create table inventory_lot (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 ingredient_id uuid not null references ingredient(id), supplier_id uuid not null references supplier(id),
 supplier_batch_number varchar(120) not null, manufacture_date date, expiry_date date,
 unit varchar(24) not null, received_at timestamp with time zone not null, created_at timestamp with time zone not null,
 unique(organization_id, supplier_id, supplier_batch_number)
);
create table stock_movement (
 id uuid primary key, organization_id uuid not null references organization(id) on delete cascade,
 lot_id uuid not null references inventory_lot(id) on delete cascade,
 from_location_id uuid references location(id), to_location_id uuid references location(id),
 movement_type varchar(30) not null, quantity numeric(19,3) not null check (quantity > 0),
 occurred_at timestamp with time zone not null, note varchar(500)
);
create index idx_inventory_lot_org on inventory_lot(organization_id);
create index idx_stock_movement_lot on stock_movement(lot_id);
create index idx_stock_movement_org on stock_movement(organization_id);
