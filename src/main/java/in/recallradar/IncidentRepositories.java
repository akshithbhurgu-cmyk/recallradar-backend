package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface IncidentRepository extends JpaRepository<Incident,UUID>{List<Incident> findByOrganizationIdOrderByCreatedAtDesc(UUID organizationId);Optional<Incident> findByIdAndOrganizationId(UUID id,UUID organizationId);}
interface LotQuarantineRepository extends JpaRepository<LotQuarantine,UUID>{boolean existsByLotIdAndActiveTrue(UUID lotId);List<LotQuarantine> findByIncidentIdOrderByCreatedAt(UUID incidentId);Optional<LotQuarantine> findByIdAndIncidentId(UUID id,UUID incidentId);}
interface CorrectiveActionRepository extends JpaRepository<CorrectiveAction,UUID>{List<CorrectiveAction> findByIncidentIdOrderByCreatedAt(UUID incidentId);Optional<CorrectiveAction> findByIdAndIncidentId(UUID id,UUID incidentId);}
