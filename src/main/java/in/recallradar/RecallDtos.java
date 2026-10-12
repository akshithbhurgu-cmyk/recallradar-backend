package in.recallradar;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.Instant; import java.util.*;
record RecallRequest(@NotNull UUID sourceLotId,@NotBlank @Size(max=180) String title){}
record RecallImpactResponse(String productionBatchId,String batchNumber,String productName,String locationName,BigDecimal affectedQuantity,String unit){}
record RecallResponse(String id,String title,RecallStatus status,String sourceLotId,String sourceIngredient,String sourceSupplierBatch,Instant createdAt,List<RecallImpactResponse> impacts){}
record RecallStatusRequest(@NotNull RecallStatus status){}
record RecallAccountingResponse(BigDecimal received,BigDecimal held,BigDecimal consumed,BigDecimal wasted,BigDecimal adjustedIn,BigDecimal adjustedOut,BigDecimal unaccounted,String unit){}
record RecallMovementEvidenceResponse(String id,String type,BigDecimal quantity,String fromLocation,String toLocation,String note,Instant occurredAt){}
record RecallProductionTraceResponse(String productionBatchId,String productionBatchNumber,String productName,BigDecimal consumedQuantity,String ingredientUnit,BigDecimal finishedOutput,String outputUnit,String locationName,Instant producedAt){}
record RecallContainmentTaskResponse(String id,String type,String description,boolean completed,Instant dueAt,Instant completedAt){}
record RecallContainmentResponse(boolean approved,Instant approvedAt,String holdStatus,List<RecallContainmentTaskResponse> tasks){}
record RecallImpactAnalysisResponse(String recallId,String title,RecallStatus status,String sourceLotId,String ingredientName,String supplierName,String supplierBatchNumber,RecallAccountingResponse accounting,List<RecallMovementEvidenceResponse> movements,List<RecallProductionTraceResponse> productionTrace,Instant generatedAt){}
