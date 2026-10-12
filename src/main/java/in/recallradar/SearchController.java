package in.recallradar;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/search")
class SearchController {
    private final SearchService search;
    private final TenantService tenants;
    SearchController(SearchService search, TenantService tenants) { this.search = search; this.tenants = tenants; }
    @GetMapping List<SearchResultResponse> search(Authentication authentication,
        @RequestHeader(value = "X-Organization-Id", required = false) String organizationId,
        @RequestParam("q") String query) {
        return search.search(tenants.membership(UUID.fromString(authentication.getName()), organizationId), query);
    }
}
