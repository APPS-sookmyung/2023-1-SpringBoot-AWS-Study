package com.hailey.book.springboot.config.auth;
import java.lang.annotation.*;
import java.lang.annotation.Retention;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {
}
