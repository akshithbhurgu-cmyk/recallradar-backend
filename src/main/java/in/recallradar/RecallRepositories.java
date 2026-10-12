package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface RecallCaseRepository extends JpaRepository<RecallCase,UUID>{List<RecallCase> findByOrganizationIdOrderByCreatedAtDesc(UUID org);Optional<RecallCase> findByIdAndOrganizationId(UUID id,UUID org);}
interface RecallImpactSnapshotRepository extends JpaRepository<RecallImpactSnapshot,UUID>{List<RecallImpactSnapshot> findByRecallCaseId(UUID id);}
interface RecallContainmentTaskRepository extends JpaRepository<RecallContainmentTask,UUID>{List<RecallContainmentTask> findByRecallCaseIdOrderByCreatedAt(UUID recallCaseId);Optional<RecallContainmentTask> findByIdAndRecallCaseId(UUID id,UUID recallCaseId);}
