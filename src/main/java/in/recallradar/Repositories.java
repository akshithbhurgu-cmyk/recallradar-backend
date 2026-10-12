package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface UserRepository extends JpaRepository<AppUser,UUID>{ Optional<AppUser> findByEmailIgnoreCase(String email); }
interface RefreshRepository extends JpaRepository<RefreshSession,UUID>{ Optional<RefreshSession> findByTokenHash(String tokenHash); }
interface ResetRepository extends JpaRepository<PasswordResetToken,UUID>{ Optional<PasswordResetToken> findByTokenHash(String tokenHash); }
