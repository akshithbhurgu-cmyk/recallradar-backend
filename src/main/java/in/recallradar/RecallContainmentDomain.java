package in.recallradar;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="recall_containment_task")
class RecallContainmentTask {
  @Id UUID id;
  UUID recallCaseId;
  String taskType;
  String description;
  boolean completed;
  Instant createdAt;
  Instant dueAt;
  Instant completedAt;
  UUID completedBy;
  protected RecallContainmentTask() {}
  RecallContainmentTask(UUID recallCaseId, String taskType, String description) {
    id=UUID.randomUUID(); this.recallCaseId=recallCaseId; this.taskType=taskType;
    this.description=description; createdAt=Instant.now(); dueAt=createdAt.plusSeconds(4 * 60 * 60);
  }
  void complete(UUID actor) { if (!completed) { completed=true; completedAt=Instant.now(); completedBy=actor; } }
}
