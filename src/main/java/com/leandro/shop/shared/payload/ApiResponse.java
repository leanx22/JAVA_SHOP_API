package com.leandro.shop.shared.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private String message;
    private boolean success;
    private T data;

    private ApiResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(message, true, data);
    }

    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(message, true, null);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(message, false, null);
    }

}
