package in.recallradar;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

record CommandRecallSummary(String id,String title,RecallStatus status,String sourceBatch,String ingredient,BigDecimal held,BigDecimal unaccounted,String unit,int affectedProductionBatches,boolean awaitingApproval){}
record CommandNoticeSummary(String id,String label,RecallNoticeStatus status,int unresolvedFacts,Instant receivedAt){}
record CommandTaskSummary(String id,String recallId,String recallTitle,String description,Instant dueAt,boolean overdue){}
record CommandSummaryResponse(List<CommandRecallSummary> openRecalls,List<CommandNoticeSummary> noticesAwaitingDecision,List<CommandTaskSummary> overdueTasks,Instant generatedAt){}
