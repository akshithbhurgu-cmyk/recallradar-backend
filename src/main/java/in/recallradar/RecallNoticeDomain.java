package in.recallradar;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

enum RecallNoticeStatus { NEEDS_REVIEW, READY_TO_CONFIRM, CONFIRMED }

@Entity @Table(name="recall_notice") class RecallNotice {
  @Id UUID id; UUID organizationId; UUID submittedBy; String sourceType; String fileName; String storedPath; String typedReference;
  @Enumerated(EnumType.STRING) RecallNoticeStatus status; UUID confirmedRecallId; Instant createdAt; Instant confirmedAt;
  protected RecallNotice(){}
  RecallNotice(UUID org,UUID actor,String type,String name,String path,String reference){id=UUID.randomUUID();organizationId=org;submittedBy=actor;sourceType=type;fileName=name;storedPath=path;typedReference=reference;status=RecallNoticeStatus.NEEDS_REVIEW;createdAt=Instant.now();}
}

@Entity @Table(name="recall_notice_fact") class RecallNoticeFact {
  @Id UUID id; UUID noticeId; String fieldName; String extractedValue; String confirmedValue; int confidence; String sourceReference; boolean requiresResolution; Instant createdAt;
  protected RecallNoticeFact(){}
  RecallNoticeFact(UUID notice,String field,String value,int score,String source,boolean unresolved){id=UUID.randomUUID();noticeId=notice;fieldName=field;extractedValue=value;confidence=score;sourceReference=source;requiresResolution=unresolved;createdAt=Instant.now();}
  String value(){return confirmedValue==null||confirmedValue.isBlank()?extractedValue:confirmedValue;}
}
