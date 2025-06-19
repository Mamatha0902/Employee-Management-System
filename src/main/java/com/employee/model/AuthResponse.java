package com.employee.model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String role;

    public String getAccessToken() {
        return accessToken;
    }
    public AuthResponse(String accessToken, String refreshToken, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public AuthResponse() {}

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
