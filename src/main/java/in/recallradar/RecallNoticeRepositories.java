package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
interface RecallNoticeRepository extends JpaRepository<RecallNotice,UUID>{List<RecallNotice> findByOrganizationIdOrderByCreatedAtDesc(UUID organizationId);Optional<RecallNotice> findByIdAndOrganizationId(UUID id,UUID organizationId);}
interface RecallNoticeFactRepository extends JpaRepository<RecallNoticeFact,UUID>{List<RecallNoticeFact> findByNoticeIdOrderByCreatedAt(UUID noticeId);Optional<RecallNoticeFact> findByIdAndNoticeId(UUID id,UUID noticeId);}
