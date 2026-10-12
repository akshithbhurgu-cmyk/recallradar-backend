package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface IntakeJobRepository extends JpaRepository<IntakeJob,UUID>{ List<IntakeJob> findByOrganizationIdOrderByCreatedAtDesc(UUID organizationId); Optional<IntakeJob> findByIdAndOrganizationId(UUID id,UUID organizationId); }
interface IntakeLineRepository extends JpaRepository<IntakeLine,UUID>{ List<IntakeLine> findByJobIdOrderByLineNumber(UUID jobId); Optional<IntakeLine> findByIdAndJobId(UUID id,UUID jobId); }
