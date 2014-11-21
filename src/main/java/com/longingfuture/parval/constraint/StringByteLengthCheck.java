package com.longingfuture.parval.constraint;

import static net.sf.oval.Validator.getCollectionFactory;

import java.util.Map;

import net.sf.oval.ConstraintTarget;
import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;
import net.sf.oval.exception.OValException;

import com.longingfuture.parval.util.ValidateUtils;

public class StringByteLengthCheck extends AbstractAnnotationCheck<StringByteLength> {

    private static final long serialVersionUID = -8336537961474884659L;

    private int min;
    private int max;

    @Override
    public void configure(final StringByteLength constraintAnnotation) {
        super.configure(constraintAnnotation);
        setMax(constraintAnnotation.max());
        setMin(constraintAnnotation.min());
    }

    @Override
    protected Map<String, String> createMessageVariables() {
        final Map<String, String> messageVariables = getCollectionFactory().createMap(2);
        messageVariables.put("max", Integer.toString(max));
        messageVariables.put("min", Integer.toString(min));
        return messageVariables;
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

        final String string = valueToValidate.toString();

        return ValidateUtils.isStringBytesLengthRight(string, min, max);
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public void setMax(final int max) {
        this.max = max;
        requireMessageVariablesRecreation();
    }

    public void setMin(final int min) {
        this.min = min;
        requireMessageVariablesRecreation();
    }
}
