package in.recallradar;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.NotEmpty; import java.math.BigDecimal; import java.time.*; import java.util.*;
record IntakeMatchCandidate(String id,String name,int confidence){}
record IntakeIssueResponse(String field,String value,String message,List<IntakeMatchCandidate> candidates){}
record IntakeLineResponse(String id,int lineNumber,String ingredientName,String supplierName,String supplierBatchNumber,LocalDate expiryDate,BigDecimal quantity,String unit,String locationName,IntakeStatus status,String issue,List<IntakeIssueResponse> issues,String receiptLotId){}
record IntakeJobResponse(String id,String fileName,IntakeStatus status,Instant createdAt,List<IntakeLineResponse> lines){}
record ConfirmIntakeRequest(@NotEmpty List<UUID> lineIds){}
record ResolveIntakeLineRequest(@NotBlank String field,UUID candidateId,boolean create){}
