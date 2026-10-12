package in.recallradar;
import jakarta.validation.constraints.*;
record InvitationRequest(@NotBlank @Email @Size(max=254) String email,@NotNull OrganizationRole role){}
record InvitationResponse(String email,OrganizationRole role,String inviteToken,String invitePath){}
record AcceptInvitationRequest(@NotBlank String token,@NotBlank @Size(max=120) String fullName,@NotBlank @Email @Size(max=254) String email,@NotBlank @Size(min=12,max=72) String password){}
