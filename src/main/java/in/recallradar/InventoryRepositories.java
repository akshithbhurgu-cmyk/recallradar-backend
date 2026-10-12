package in.recallradar;
import org.springframework.data.jpa.repository.*; import jakarta.persistence.LockModeType; import java.util.*;
interface InventoryLotRepository extends JpaRepository<InventoryLot,UUID>{ List<InventoryLot> findByOrganizationIdOrderByReceivedAtDesc(UUID org); Optional<InventoryLot> findByIdAndOrganizationId(UUID id,UUID org); @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select l from InventoryLot l where l.id=:id and l.organizationId=:org") Optional<InventoryLot> findLockedByIdAndOrganizationId(UUID id,UUID org); }
interface StockMovementRepository extends JpaRepository<StockMovement,UUID>{ List<StockMovement> findByLotIdOrderByOccurredAt(UUID lotId); }
interface InventoryBalanceRepository extends JpaRepository<InventoryBalance,UUID>{ List<InventoryBalance> findByLotId(UUID lotId); Optional<InventoryBalance> findByLotIdAndLocationId(UUID lotId,UUID locationId); }
interface InventoryCommandRepository extends JpaRepository<InventoryCommand,UUID>{ Optional<InventoryCommand> findByOrganizationIdAndActorIdAndIdempotencyKey(UUID organizationId,UUID actorId,String idempotencyKey); }
interface AuditEventRepository extends JpaRepository<AuditEvent,UUID>{ List<AuditEvent> findTop20ByOrganizationIdOrderByOccurredAtDesc(UUID organizationId); }
