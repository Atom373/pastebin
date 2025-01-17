package com.example.pastebin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.pastebin.validators.UniquenessValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=UniquenessValidator.class)
public @interface Unique {

    String message();
    
    Class <?> [] groups() default {};
    
    Class <? extends Payload>[] payload() default {};
}
