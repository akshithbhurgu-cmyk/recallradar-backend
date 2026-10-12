package in.recallradar;

import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/command")
class CommandSummaryController {
  private final CommandSummaryService service; private final TenantService tenants;
  CommandSummaryController(CommandSummaryService service,TenantService tenants){this.service=service;this.tenants=tenants;}
  @GetMapping("/summary") CommandSummaryResponse summary(Authentication authentication,@RequestHeader(value="X-Organization-Id",required=false) String organization){return service.summary(tenants.membership(UUID.fromString(authentication.getName()),organization));}
}
