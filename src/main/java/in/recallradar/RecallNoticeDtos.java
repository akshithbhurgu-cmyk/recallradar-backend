package in.recallradar;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;
record RecallNoticeFactResponse(String id,String field,String extractedValue,String confirmedValue,int confidence,String sourceReference,boolean requiresResolution){}
record RecallNoticeCandidateResponse(String lotId,String ingredientName,String supplierName,String supplierBatchNumber,String expiryDate,String availableQuantity,String unit,int score,List<String> reasons){}
record RecallNoticeResponse(String id,RecallNoticeStatus status,String sourceType,String fileName,String typedReference,Instant createdAt,List<RecallNoticeFactResponse> facts,List<RecallNoticeCandidateResponse> candidates,String confirmedRecallId){}
record RecallNoticeReferenceRequest(@NotBlank @Size(max=500) String reference){}
record ConfirmRecallNoticeFactRequest(@NotBlank @Size(max=1000) String value){}
record ConfirmRecallNoticeRequest(@NotNull UUID lotId,@Size(max=180) String title){}
