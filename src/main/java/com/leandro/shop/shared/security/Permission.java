package com.leandro.shop.shared.security;

public enum Permission {
    USER_CREATE("user:create"),

    USER_UPDATE_BASIC("user:update:basic"),
    USER_UPDATE_ADVANCE("user:update:basic"),

    USER_PASSWORD_MODIFICATION_OWN("user:password:modification:own"),
    USER_PASSWORD_MODIFICATION_ANY("user:password:modification:any"),

    USER_DELETE_ADVANCED("user:delete:advanced"),
    USER_DELETE_OWN("user:delete:own"),

    USER_READ_BASIC("user:read:basic"),
    USER_READ_ADVANCE("user:read:advance"),


    PRODUCT_CREATE("product:create"),

    PRODUCT_UPDATE_OWN("product:update:own"),
    PRODUCT_UPDATE_ANY("product:update:any"),

    PRODUCT_DELETE_ADVANCED("product:delete:advanced"),
    PRODUCT_DELETE_OWN("product:delete:own"),

    PRODUCT_READ_BASIC("user:read:basic"),
    PRODUCT_READ_ADVANCE("user:read:advance");


    private final String permission;

    Permission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
