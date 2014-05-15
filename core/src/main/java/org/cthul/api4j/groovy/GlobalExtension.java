package org.cthul.api4j.groovy;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalExtension {
    
    Class<?> value() default Object.class;
}
