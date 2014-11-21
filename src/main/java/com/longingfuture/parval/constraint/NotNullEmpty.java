package com.longingfuture.parval.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.configuration.annotation.Constraint;
import net.sf.oval.configuration.annotation.Constraints;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Constraint(checkWith = NotNullEmptyCheck.class)
public @interface NotNullEmpty {

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
    @Constraints
    public @interface List {

        /**
         * The NotNull constraints.
         */
        NotNullEmpty[] value();

        /**
         * Formula returning <code>true</code> if this constraint shall be evaluated and
         * <code>false</code> if it shall be ignored for the current validation.
         * <p>
         * <b>Important:</b> The formula must be prefixed with the name of the scripting language that is used.
         * E.g. <code>groovy:_this.amount > 10</code>
         * <p>
         * Available context variables are:<br>
         * <b>_this</b> -&gt; the validated bean<br>
         * <b>_value</b> -&gt; the value to validate (e.g. the field value, parameter value, method return value,
         *    or the validated bean for object level constraints)
         */
        String when() default "";
    }

    /**
     * <p>In case the constraint is declared for an array, collection or map this controls how the constraint is applied to it and it's child objects.
     * 
     * <p><b>Default:</b> ConstraintTarget.CONTAINER, ConstraintTarget.VALUES
     * 
     * <p><b>Note:</b> This setting is ignored for object types other than array, map and collection.
     */
    ConstraintTarget[] appliesTo() default { ConstraintTarget.CONTAINER, ConstraintTarget.VALUES };

    /**
     * error code passed to the ConstraintViolation object
     */
    String errorCode() default "com.sogou.bizparser.constraint.NotNull";

    /**
     * message to be used for the ContraintsViolatedException
     * 
     * @see ConstraintViolation
     */
    String message() default "com.sogou.bizparser.constraint.NotNull.violated";

    /**
     * The associated constraint profiles.
     */
    String[] profiles() default {};

    /**
     * severity passed to the ConstraintViolation object
     */
    int severity() default 0;

    String target() default "";

    /**
     * Formula returning <code>true</code> if this constraint shall be evaluated and
     * <code>false</code> if it shall be ignored for the current validation.
     * <p>
     * <b>Important:</b> The formula must be prefixed with the name of the scripting language that is used.
     * E.g. <code>groovy:_this.amount > 10</code>
     * <p>
     * Available context variables are:<br>
     * <b>_this</b> -&gt; the validated bean<br>
     * <b>_value</b> -&gt; the value to validate (e.g. the field value, parameter value, method return value,
     *    or the validated bean for object level constraints)
     */
    String when() default "";
}
