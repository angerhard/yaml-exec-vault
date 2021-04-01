package de.andreasgerhard.vault.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(
    value = RetentionPolicy.RUNTIME
)
@Target(ElementType.PARAMETER)
public @interface Tag {

  String value();

}
