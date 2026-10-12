package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface OrganizationRepository extends JpaRepository<Organization,UUID>{ boolean existsBySlug(String slug); }
interface MembershipRepository extends JpaRepository<Membership,UUID>{ List<Membership> findByUserId(UUID userId); Optional<Membership> findByUserIdAndOrganizationId(UUID userId,UUID organizationId); }
interface LocationRepository extends JpaRepository<Location,UUID>{ List<Location> findByOrganizationIdOrderByName(UUID id); Optional<Location> findByIdAndOrganizationId(UUID id,UUID org); }
interface SupplierRepository extends JpaRepository<Supplier,UUID>{ List<Supplier> findByOrganizationIdOrderByName(UUID id); Optional<Supplier> findByIdAndOrganizationId(UUID id,UUID org); }
interface IngredientRepository extends JpaRepository<Ingredient,UUID>{ List<Ingredient> findByOrganizationIdOrderByName(UUID id); Optional<Ingredient> findByIdAndOrganizationId(UUID id,UUID org); }
interface SupplierIngredientRepository extends JpaRepository<SupplierIngredient,SupplierIngredientId>{ List<SupplierIngredient> findBySupplierId(UUID supplierId); }
