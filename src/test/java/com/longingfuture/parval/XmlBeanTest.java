package com.longingfuture.parval;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.longingfuture.parval.BeanParserBuilder;
import com.longingfuture.parval.annotation.Source;
import com.longingfuture.parval.annotation.SourceType;
import com.longingfuture.parval.core.BeanParser;

public class XmlBeanTest extends TestCase {

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "persons")
    @Source(SourceType.XML)
    public static class Persons {

        @XmlElement(name = "person", required = true)
        private List<Person> person;

        public List<Person> getPerson() {
            return person;
        }

        public void setPerson(List<Person> person) {
            this.person = person;
        }

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "person")
    public static class Person {

        @XmlElement(name = "name", required = true)
        private String name;
        @XmlElement(name = "age", required = true)
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
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        builder.append("<persons>");
        builder.append("<person>");
        builder.append("<name>alan</name>");
        builder.append("<age>27</age>");
        builder.append("</person>");
        builder.append("<person>");
        builder.append("<name>shangyan</name>");
        builder.append("<age>27</age>");
        builder.append("</person>");
        builder.append("</persons>");

        InputStream in = new ByteArrayInputStream(builder.toString().getBytes("utf-8"));
        BeanParser parser = BeanParserBuilder.build();
        List<Persons> list = parser.parse(in, Persons.class);
        Assert.assertTrue(list.size() == 1);

        List<Person> personList = list.get(0).getPerson();
        for (Person person : personList) {
            System.out.println(person.getName() + ":" + person.getAge());
        }
    }
}
