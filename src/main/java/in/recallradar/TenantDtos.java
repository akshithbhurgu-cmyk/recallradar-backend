package in.recallradar;
import jakarta.validation.constraints.*; import java.util.*;
record OrganizationRequest(@NotBlank @Size(max=160) String name){}
record OrganizationResponse(String id,String name,String slug,String role){}
record MembershipRequest(@NotBlank @Email String email,@NotNull OrganizationRole role){}
record MembershipResponse(String id,String userId,String email,String fullName,OrganizationRole role){}
record RoleUpdateRequest(@NotNull OrganizationRole role){}
record LocationRequest(@NotBlank @Size(max=160) String name,@NotNull LocationType type,@Size(max=500) String address){}
record LocationResponse(String id,String name,LocationType type,String address,boolean active){}
record SupplierRequest(@NotBlank @Size(max=180) String name,@Size(max=120) String contactName,@Email String email,@Size(max=40) String phone){}
record SupplierResponse(String id,String name,String contactName,String email,String phone,boolean active,List<String> ingredientIds){}
record IngredientRequest(@NotBlank @Size(max=180) String name,@Size(max=100) String category,@NotBlank @Size(max=24) String unit){}
record IngredientResponse(String id,String name,String category,String unit,boolean active){}
record SupplierIngredientRequest(@NotNull UUID ingredientId,@Size(max=100) String supplierSku){}
