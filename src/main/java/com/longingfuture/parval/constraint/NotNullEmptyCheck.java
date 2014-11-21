package com.longingfuture.parval.constraint;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;

public class NotNullEmptyCheck extends AbstractAnnotationCheck<NotNullEmpty> {

    private static final long serialVersionUID = 1L;

    /**
     * {@inheritDoc}
     */
    public boolean isSatisfied(final Object validatedObject, final Object valueToValidate, final OValContext context,
            final Validator validator) {

        if (valueToValidate == null) {
            return false;
        } else {
            return valueToValidate.toString().length() > 0;
        }
    }
}
