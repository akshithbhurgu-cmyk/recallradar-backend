package in.recallradar;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
record ProductRequest(@NotBlank @Size(max=180) String name,@Size(max=80) String sku,@NotBlank @Size(max=24) String unit){}
record ProductResponse(String id,String name,String sku,String unit,boolean active){}
record RecipeLineRequest(@NotNull UUID ingredientId,@NotNull @DecimalMin("0.001") BigDecimal quantity,@NotBlank @Size(max=24) String unit){}
record RecipeRequest(@NotNull UUID productId,@NotBlank @Size(max=180) String name,@NotNull @DecimalMin("0.001") BigDecimal yieldQuantity,@NotBlank @Size(max=24) String yieldUnit,@NotEmpty List<@Valid RecipeLineRequest> ingredients){}
record RecipeLineResponse(String ingredientId,String ingredientName,BigDecimal quantity,String unit){}
record RecipeResponse(String id,String productId,String productName,String name,BigDecimal yieldQuantity,String yieldUnit,boolean active,List<RecipeLineResponse> ingredients){}
record ConsumptionRequest(@NotNull UUID lotId,@NotNull @DecimalMin("0.001") BigDecimal quantity){}
record ProductionRequest(@NotNull UUID recipeId,@NotNull UUID locationId,@NotBlank @Size(max=120) String batchNumber,@NotNull @DecimalMin("0.001") BigDecimal outputQuantity,@NotEmpty List<@Valid ConsumptionRequest> consumptions,@Size(max=500) String note){}
record ConsumptionResponse(String lotId,String ingredientName,String supplierBatchNumber,BigDecimal quantity,String unit){}
record ProductionResponse(String id,String recipeName,String productName,String locationName,String batchNumber,BigDecimal outputQuantity,String outputUnit,Instant producedAt,String note,List<ConsumptionResponse> consumptions){}
record ProductionPlanRequest(@NotNull UUID recipeId,@NotNull UUID locationId,@NotNull @DecimalMin("0.001") BigDecimal outputQuantity,@Size(max=500) String note){}
record ProductionPlanLine(String ingredientName,String unit,BigDecimal requiredQuantity,BigDecimal allocatedQuantity,List<ConsumptionResponse> lots){}
record ProductionPlanResponse(String proposedBatchNumber,String recipeName,String productName,String locationName,BigDecimal outputQuantity,String outputUnit,List<ProductionPlanLine> lines,boolean ready,String issue){}
