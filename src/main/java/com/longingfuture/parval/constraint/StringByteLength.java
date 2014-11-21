package com.longingfuture.parval.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.configuration.annotation.Constraint;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Constraint(checkWith = StringByteLengthCheck.class)
public @interface StringByteLength {

    public @interface List {

        StringByteLength[] values();

        String when() default "";
    }

    ConstraintTarget[] appliesTo() default ConstraintTarget.VALUES;

    String errorCode() default "com.sogou.bizparser.constraint.StringByteLength";

    int max() default Integer.MAX_VALUE;

    String message() default "com.sogou.bizparser.constraint.StringByteLength.violated";

    int min() default 0;

    String[] profiles() default {};

    int severity() default 0;

    String target() default "";

    String when() default "";
}
