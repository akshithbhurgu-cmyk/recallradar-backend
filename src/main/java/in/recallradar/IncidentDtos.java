package in.recallradar;
import jakarta.validation.constraints.*; import java.time.*; import java.util.*;
record IncidentCreateRequest(@NotBlank @Size(max=180) String title,@NotNull IncidentSeverity severity,@NotEmpty List<UUID> lotIds,@Size(max=500) String quarantineReason){}
record IncidentStatusRequest(@NotNull IncidentStatus status){}
record CorrectiveActionRequest(@NotBlank @Size(max=500) String description){}
record QuarantineResponse(String id,String lotId,String ingredientName,String supplierBatchNumber,boolean active,String reason,Instant createdAt,Instant releasedAt){}
record CorrectiveActionResponse(String id,String description,boolean completed,Instant createdAt,Instant completedAt){}
record IncidentResponse(String id,String title,IncidentStatus status,IncidentSeverity severity,Instant createdAt,List<QuarantineResponse> quarantines,List<CorrectiveActionResponse> actions){}
