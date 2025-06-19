package com.employee.exceptionhandling;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
public class ApiResponse<T> {
    private T results;
    private String errorMessage;
    private String errorCode;

    public ApiResponse() {
    }

    public ApiResponse(T results, String errorMessage, String errorCode) {
        this.results = results;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static <T> ApiResponse<T> success(T results) {
        return new ApiResponse<>(results, null, "200");
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(null, message, code);
    }
}
