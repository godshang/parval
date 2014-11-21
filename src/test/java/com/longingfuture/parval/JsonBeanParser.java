package com.longingfuture.parval;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.longingfuture.parval.BeanParserBuilder;
import com.longingfuture.parval.annotation.Source;
import com.longingfuture.parval.annotation.SourceType;
import com.longingfuture.parval.core.BeanParser;

public class JsonBeanParser extends TestCase {

    @Source(SourceType.JSON)
    @JsonSerialize(include = Inclusion.NON_NULL)
    public static class Person {

        @JsonProperty("name")
        private String name;
        @JsonProperty("age")
        private String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

    }

    public void test() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append("[{");
        builder.append("\"name\": \"alan\",");
        builder.append("\"age\": 27");
        builder.append("}]");

        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("utf-8"));
        BeanParser parser = BeanParserBuilder.build();
        List<Person> list = parser.parse(in, Person.class);
        Assert.assertTrue(list.size() == 1);

        for (Person item : list) {
            System.out.println(item.getName() + ":" + item.getAge());
        }
    }
}
