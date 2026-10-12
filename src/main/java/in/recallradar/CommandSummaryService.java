package in.recallradar;

import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly=true)
class CommandSummaryService {
  private final RecallCaseRepository recallCases; private final RecallNoticeRepository notices;
  private final RecallNoticeFactRepository noticeFacts; private final RecallContainmentTaskRepository tasks;
  private final RecallService recalls;
  CommandSummaryService(RecallCaseRepository recallCases, RecallNoticeRepository notices, RecallNoticeFactRepository noticeFacts, RecallContainmentTaskRepository tasks, RecallService recalls) {
    this.recallCases=recallCases; this.notices=notices; this.noticeFacts=noticeFacts; this.tasks=tasks; this.recalls=recalls;
  }
  CommandSummaryResponse summary(Membership member) {
    var active=recallCases.findByOrganizationIdOrderByCreatedAtDesc(member.organizationId).stream().filter(x -> x.status!=RecallStatus.CLOSED).toList();
    var recallItems=active.stream().map(recall -> {
      var impact=recalls.impact(member,recall.id); var accounting=impact.accounting();
      boolean awaiting=recall.containmentIncidentId==null;
      return new CommandRecallSummary(recall.id.toString(),recall.title,recall.status,impact.supplierBatchNumber(),impact.ingredientName(),accounting.held(),accounting.unaccounted(),accounting.unit(),impact.productionTrace().size(),awaiting);
    }).sorted(Comparator.comparing(CommandRecallSummary::awaitingApproval).reversed().thenComparing(x -> x.unaccounted().signum()!=0?0:1)).limit(12).toList();
    var pendingNotices=notices.findByOrganizationIdOrderByCreatedAtDesc(member.organizationId).stream().filter(x -> x.status!=RecallNoticeStatus.CONFIRMED).map(n -> new CommandNoticeSummary(n.id.toString(),n.fileName==null?(n.typedReference==null?"Recall notice":n.typedReference):n.fileName,n.status,(int)noticeFacts.findByNoticeIdOrderByCreatedAt(n.id).stream().filter(f -> f.requiresResolution).count(),n.createdAt)).limit(8).toList();
    Instant now=Instant.now();
    var overdue=active.stream().flatMap(recall -> tasks.findByRecallCaseIdOrderByCreatedAt(recall.id).stream().filter(task -> !task.completed && task.dueAt.isBefore(now)).map(task -> new CommandTaskSummary(task.id.toString(),recall.id.toString(),recall.title,task.description,task.dueAt,true))).sorted(Comparator.comparing(CommandTaskSummary::dueAt)).limit(8).toList();
    return new CommandSummaryResponse(recallItems,pendingNotices,overdue,now);
  }
}
