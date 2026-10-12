package in.recallradar;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
@RestController @RequestMapping("/api/recall-notices") class RecallNoticeController {
 private final RecallNoticeService service; private final TenantService tenants;
 RecallNoticeController(RecallNoticeService s,TenantService t){service=s;tenants=t;}
 private Membership member(Authentication a,String org){return tenants.membership(UUID.fromString(a.getName()),org);}
 @GetMapping List<RecallNoticeResponse> list(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org){return service.list(member(a,org));}
 @GetMapping("/{id}") RecallNoticeResponse get(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org,@PathVariable UUID id){return service.get(member(a,org),id);}
 @PostMapping(value="/upload",consumes=MediaType.MULTIPART_FORM_DATA_VALUE) @ResponseStatus(HttpStatus.CREATED) RecallNoticeResponse upload(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org,@RequestPart("file") MultipartFile file){return service.upload(member(a,org),file);}
 @PostMapping("/reference") @ResponseStatus(HttpStatus.CREATED) RecallNoticeResponse reference(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org,@Valid @RequestBody RecallNoticeReferenceRequest request){return service.reference(member(a,org),request);}
 @PatchMapping("/{noticeId}/facts/{factId}") RecallNoticeResponse confirmFact(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org,@PathVariable UUID noticeId,@PathVariable UUID factId,@Valid @RequestBody ConfirmRecallNoticeFactRequest request){return service.confirmFact(member(a,org),noticeId,factId,request);}
 @PostMapping("/{id}/confirm") RecallNoticeResponse confirm(Authentication a,@RequestHeader(value="X-Organization-Id",required=false)String org,@PathVariable UUID id,@Valid @RequestBody ConfirmRecallNoticeRequest request){return service.confirm(member(a,org),id,request);}
}
