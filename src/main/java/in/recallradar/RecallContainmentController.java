package in.recallradar;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recalls/{recallId}/containment")
class RecallContainmentController {
  private final RecallContainmentService service; private final TenantService tenants;
  RecallContainmentController(RecallContainmentService service, TenantService tenants) { this.service=service; this.tenants=tenants; }
  private Membership member(Authentication authentication, String organization) { return tenants.membership(UUID.fromString(authentication.getName()),organization); }
  @GetMapping RecallContainmentResponse get(Authentication authentication,@RequestHeader(value="X-Organization-Id",required=false) String organization,@PathVariable UUID recallId) { return service.get(member(authentication,organization),recallId); }
  @PostMapping("/approve") RecallContainmentResponse approve(Authentication authentication,@RequestHeader(value="X-Organization-Id",required=false) String organization,@PathVariable UUID recallId) { return service.approve(member(authentication,organization),recallId); }
  @PostMapping("/tasks/{taskId}/complete") RecallContainmentResponse complete(Authentication authentication,@RequestHeader(value="X-Organization-Id",required=false) String organization,@PathVariable UUID recallId,@PathVariable UUID taskId) { return service.complete(member(authentication,organization),recallId,taskId); }
}
