package in.recallradar;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service class AuditService {
 private final AuditEventRepository events;
 AuditService(AuditEventRepository events){this.events=events;}
 void record(Membership membership,String action,String entityType,UUID entityId,String details){
  var authentication=SecurityContextHolder.getContext().getAuthentication();
  UUID actorId=authentication==null?membership.userId:UUID.fromString(authentication.getName());
  events.save(new AuditEvent(membership.organizationId,actorId,action,entityType,entityId,details));
 }
}

@jakarta.persistence.Entity @jakarta.persistence.Table(name="audit_event") class AuditEvent {
 @jakarta.persistence.Id UUID id; UUID organizationId; UUID actorId; String action; String entityType; UUID entityId; String details; Instant occurredAt;
 protected AuditEvent(){}
 AuditEvent(UUID organizationId,UUID actorId,String action,String entityType,UUID entityId,String details){this.id=UUID.randomUUID();this.organizationId=organizationId;this.actorId=actorId;this.action=action;this.entityType=entityType;this.entityId=entityId;this.details=details;this.occurredAt=Instant.now();}
}
