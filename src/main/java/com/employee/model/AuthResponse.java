package com.employee.model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }
    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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
}
