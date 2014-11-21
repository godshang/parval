package com.longingfuture.parval;

import junit.framework.TestCase;
import net.sf.oval.constraint.Max;
import net.sf.oval.constraint.Min;

import com.longingfuture.parval.BeanValidatorBuilder;
import com.longingfuture.parval.constraint.Digits;
import com.longingfuture.parval.constraint.NotNullEmpty;
import com.longingfuture.parval.constraint.Numeric;
import com.longingfuture.parval.constraint.StringByteLength;
import com.longingfuture.parval.core.BeanValidator;
import com.longingfuture.parval.result.BeanWrapper;

public class BeanValidatorTest extends TestCase {

    public static class Item {

        @NotNullEmpty(message = "price必填项，不能为空")
        @Numeric(when = "js:_value != null && _value != ''", message = "price必须是数字")
        @StringByteLength(when = "js:_value != null && _value !=''", max = 100, message = "price要求<={max}字节")
        @Digits(when = "js:_value != null && _value != '' && !isNaN(_value)", maxFraction = 2, message = "price小数点后不能超过{maxFraction}位")
        @Max(when = "js:_value != null && _value != '' && !isNaN(_value)", value = 999.99, message = "price价格不能高于{max}元")
        @Min(when = "js:_value != null && _value != '' && !isNaN(_value)", value = 1, message = "price价格不能低于底价(底价={min})")
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }

    public void test() throws Exception {
        Item item = new Item();
        item.setPrice("1000.056");
        BeanValidator validator = BeanValidatorBuilder.build();
        BeanWrapper<Item> wrapper = validator.validate(item);
        System.out.println(wrapper.getErrorMsgs());
    }
}
