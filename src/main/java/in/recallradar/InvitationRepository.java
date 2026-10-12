package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface TeamInvitationRepository extends JpaRepository<TeamInvitation,UUID>{Optional<TeamInvitation> findByTokenHash(String tokenHash);}
