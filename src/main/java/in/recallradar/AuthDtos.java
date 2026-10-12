package in.recallradar;
import jakarta.validation.constraints.*;
record RegisterRequest(@NotBlank @Size(max=120) String fullName,@Size(max=160) String organizationName,@NotBlank @Email @Size(max=254) String email,@NotBlank @Size(min=12,max=72) String password,@NotBlank String setupCode) {}
record LoginRequest(@NotBlank @Email String email,@NotBlank String password) {}
record RefreshRequest(@NotBlank String refreshToken) {}
record ResetRequest(@NotBlank @Email String email) {}
record ResetConfirmRequest(@NotBlank String token,@NotBlank @Size(min=12,max=72) String password) {}
record TokenResponse(String accessToken,String refreshToken,String tokenType,long expiresIn) {}
record UserResponse(String id,String fullName,String email,String role) {}
record ResetResponse(String message,String developmentToken) {}
