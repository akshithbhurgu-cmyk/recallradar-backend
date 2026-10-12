package in.recallradar;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.*; import java.util.*;
record ReceiveLotRequest(@NotNull UUID ingredientId,@NotNull UUID supplierId,@NotBlank @Size(max=120) String supplierBatchNumber,LocalDate manufactureDate,LocalDate expiryDate,@NotNull UUID locationId,@NotNull @DecimalMin(value="0.001") BigDecimal quantity,@NotBlank @Size(max=24) String unit,@Size(max=500) String note){}
record TransferRequest(@NotNull UUID fromLocationId,@NotNull UUID toLocationId,@NotNull @DecimalMin(value="0.001") BigDecimal quantity,@Size(max=500) String note){}
record AdjustmentRequest(@NotNull UUID locationId,@NotNull @DecimalMin(value="-999999999.999",inclusive=false) @DecimalMax(value="999999999.999",inclusive=false) BigDecimal quantity,@Size(max=500) String note){}
record StockReductionRequest(@NotNull UUID locationId,@NotNull @DecimalMin(value="0.001") BigDecimal quantity,@Size(max=500) String note){}
record LotResponse(String id,String ingredientId,String ingredientName,String supplierId,String supplierName,String supplierBatchNumber,LocalDate manufactureDate,LocalDate expiryDate,String unit,Instant receivedAt){}
record BalanceResponse(String locationId,String locationName,BigDecimal quantity,String unit){}
record InventoryLotResponse(LotResponse lot,List<BalanceResponse> balances,BigDecimal totalQuantity){}
record MovementResponse(String id,MovementType type,String fromLocationId,String fromLocationName,String toLocationId,String toLocationName,BigDecimal quantity,String note,Instant occurredAt){}
record TraceabilityResponse(LotResponse lot,List<MovementResponse> movements,List<BalanceResponse> currentBalances){}
