package in.recallradar;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/recalls/{recallId}/assistant")
class RecallAssistantController { private final RecallAssistantService service; private final TenantService tenants; RecallAssistantController(RecallAssistantService service,TenantService tenants){this.service=service;this.tenants=tenants;} @GetMapping RecallAssistantResponse explain(Authentication authentication,@RequestHeader(value="X-Organization-Id",required=false) String organization,@PathVariable UUID recallId){return service.explain(tenants.membership(UUID.fromString(authentication.getName()),organization),recallId);} }
