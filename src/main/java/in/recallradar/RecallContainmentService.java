package in.recallradar;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service @Transactional
class RecallContainmentService {
  private final RecallCaseRepository recalls;
  private final RecallContainmentTaskRepository tasks;
  private final IncidentRepository incidents;
  private final LotQuarantineRepository quarantines;
  private final InventoryLotRepository lots;
  private final InventoryBalanceRepository balances;
  private final TenantService tenants;
  private final AuditService audit;

  RecallContainmentService(RecallCaseRepository recalls, RecallContainmentTaskRepository tasks,
      IncidentRepository incidents, LotQuarantineRepository quarantines, InventoryLotRepository lots,
      InventoryBalanceRepository balances, TenantService tenants, AuditService audit) {
    this.recalls=recalls; this.tasks=tasks; this.incidents=incidents; this.quarantines=quarantines;
    this.lots=lots; this.balances=balances; this.tenants=tenants; this.audit=audit;
  }

  RecallContainmentResponse get(Membership member, UUID recallId) {
    return response(recall(member, recallId));
  }

  RecallContainmentResponse approve(Membership member, UUID recallId) {
    edit(member); var recall=recall(member, recallId);
    if (recall.status==RecallStatus.CLOSED) throw conflict("A closed recall cannot be contained.");
    if (recall.containmentIncidentId != null) return response(recall);
    var lot=lots.findByIdAndOrganizationId(recall.sourceLotId,member.organizationId).orElseThrow(() -> missing("Source lot"));
    BigDecimal held=balances.findByLotId(lot.id).stream().map(x -> x.quantity).reduce(BigDecimal.ZERO,BigDecimal::add);
    var incident=incidents.save(new Incident(member.organizationId,"Recall containment — "+recall.title,IncidentSeverity.CRITICAL,member.userId));
    quarantines.save(new LotQuarantine(incident.id,lot.id,"Approved recall containment"));
    recall.containmentIncidentId=incident.id;
    recall.status=RecallStatus.REVIEWING;
    tasks.save(new RecallContainmentTask(recall.id,"HOLD_SOURCE_LOT","Verify that "+held.stripTrailingZeros().toPlainString()+" "+lot.unit+" of lot "+lot.supplierBatchNumber+" is held and unavailable for use."));
    tasks.save(new RecallContainmentTask(recall.id,"PAUSE_AFFECTED_OUTPUT","Pause affected dishes and confirm the production impact has been communicated."));
    audit.record(member,"RECALL_CONTAINMENT_APPROVED","RECALL_CASE",recall.id,"Source lot quarantined and containment tasks created");
    return response(recall);
  }

  RecallContainmentResponse complete(Membership member, UUID recallId, UUID taskId) {
    edit(member); var recall=recall(member, recallId);
    if (recall.containmentIncidentId==null) throw conflict("Approve the response before completing containment tasks.");
    var task=tasks.findByIdAndRecallCaseId(taskId,recall.id).orElseThrow(() -> missing("Containment task"));
    task.complete(member.userId);
    audit.record(member,"RECALL_CONTAINMENT_TASK_COMPLETED","RECALL_CASE",recall.id,task.taskType);
    if (tasks.findByRecallCaseIdOrderByCreatedAt(recall.id).stream().allMatch(x -> x.completed)) {
      recall.status=RecallStatus.CONTAINED;
      audit.record(member,"RECALL_CONTAINED","RECALL_CASE",recall.id,"All containment tasks completed");
    }
    return response(recall);
  }

  private RecallContainmentResponse response(RecallCase recall) {
    var listed=tasks.findByRecallCaseIdOrderByCreatedAt(recall.id);
    var views=listed.stream().map(x -> new RecallContainmentTaskResponse(x.id.toString(),x.taskType,x.description,x.completed,x.dueAt,x.completedAt)).toList();
    var approvedAt=listed.isEmpty()?null:listed.get(0).createdAt;
    return new RecallContainmentResponse(recall.containmentIncidentId!=null,approvedAt,recall.containmentIncidentId==null?"Not yet held":"Source lot is quarantined",views);
  }
  private RecallCase recall(Membership m, UUID id) { return recalls.findByIdAndOrganizationId(id,m.organizationId).orElseThrow(() -> missing("Recall")); }
  private void edit(Membership m) { tenants.requireMasterDataEditor(m); }
  private ResponseStatusException missing(String item) { return new ResponseStatusException(HttpStatus.NOT_FOUND,item+" not found."); }
  private ResponseStatusException conflict(String detail) { return new ResponseStatusException(HttpStatus.CONFLICT,detail); }
}
