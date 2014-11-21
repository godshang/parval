package com.longingfuture.parval.constraint;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

public class NumericChecker extends AbstractAnnotationCheck<Numeric> {

    private static final long serialVersionUID = 2350239510592832591L;

    @Override
    public void configure(final Numeric constraintAnnotation) {
        super.configure(constraintAnnotation);
    }

    @Override
    protected ConstraintTarget[] getAppliesToDefault() {
        return new ConstraintTarget[] { ConstraintTarget.VALUES };
    }

    @Override
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator)
            throws OValException {

        if (valueToValidate == null)
            return true;

        if (valueToValidate instanceof Number)
            return true;

        final String stringValue = valueToValidate.toString();

        try {
            Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
