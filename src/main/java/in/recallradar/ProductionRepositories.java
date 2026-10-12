package in.recallradar;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
interface FinishedProductRepository extends JpaRepository<FinishedProduct,UUID>{List<FinishedProduct> findByOrganizationIdOrderByName(UUID org);Optional<FinishedProduct> findByIdAndOrganizationId(UUID id,UUID org);}
interface RecipeRepository extends JpaRepository<Recipe,UUID>{List<Recipe> findByOrganizationIdOrderByCreatedAtDesc(UUID org);Optional<Recipe> findByIdAndOrganizationId(UUID id,UUID org);}
interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient,UUID>{List<RecipeIngredient> findByRecipeId(UUID recipeId);}
interface ProductionBatchRepository extends JpaRepository<ProductionBatch,UUID>{List<ProductionBatch> findByOrganizationIdOrderByProducedAtDesc(UUID org);boolean existsByOrganizationIdAndBatchNumber(UUID org,String batchNumber);}
interface ProductionConsumptionRepository extends JpaRepository<ProductionConsumption,UUID>{List<ProductionConsumption> findByProductionBatchId(UUID batchId);List<ProductionConsumption> findByLotId(UUID lotId);}
