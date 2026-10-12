package in.recallradar;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly=true)
class RecallAssistantService {
  private final RecallService recalls;
  RecallAssistantService(RecallService recalls){this.recalls=recalls;}
  RecallAssistantResponse explain(Membership member, UUID recallId) {
    var impact=recalls.impact(member,recallId); var a=impact.accounting();
    String quantity=a.held().stripTrailingZeros().toPlainString()+" "+a.unit();
    String consumed=a.consumed().stripTrailingZeros().toPlainString()+" "+a.unit();
    String summary=impact.ingredientName()+" lot "+impact.supplierBatchNumber()+" has "+quantity+" still held and "+consumed+" recorded in production. "+impact.productionTrace().size()+" production batch"+(impact.productionTrace().size()==1?" is":"es are")+" linked to this recall.";
    var recommendations=new ArrayList<String>();
    if(a.unaccounted().compareTo(BigDecimal.ZERO)!=0) recommendations.add("Investigate the "+a.unaccounted().stripTrailingZeros().toPlainString()+" "+a.unit()+" unaccounted quantity before closing the recall.");
    if(a.held().compareTo(BigDecimal.ZERO)>0) recommendations.add("Keep the remaining "+quantity+" quarantined until containment is complete.");
    if(!impact.productionTrace().isEmpty()) recommendations.add("Pause and verify the affected dishes before returning them to service.");
    if(recommendations.isEmpty()) recommendations.add("Review the evidence trail and confirm the operational response before closing the recall.");
    return new RecallAssistantResponse("EVIDENCE_ASSIST",summary,recommendations,List.of("Stock movement ledger","Production consumption records","Current inventory balance"),"This assistant explains recorded evidence and suggests next steps. A person must still confirm notices, approve containment, and complete tasks.");
  }
}
