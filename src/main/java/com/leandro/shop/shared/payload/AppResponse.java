package com.leandro.shop.shared.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppResponse<T> {
    private String message;
    private boolean success;
    private T data;

    private AppResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static <T> AppResponse<T> success(String message, T data){
        return new AppResponse<>(message, true, data);
    }

    public static <T> AppResponse<T> success(String message){
        return new AppResponse<>(message, true, null);
    }

    public static <T> AppResponse<T> error(String message){
        return new AppResponse<>(message, false, null);
    }

}
