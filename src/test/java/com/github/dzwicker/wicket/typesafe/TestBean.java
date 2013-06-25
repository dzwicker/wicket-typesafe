package com.github.dzwicker.wicket.typesafe;

public class TestBean implements TestInterface {

    public TestBean(final String justNotANoArgs) {
    }

    @Override
    public String getString() {
        return "ID";
    }

    @Override
    @SuppressWarnings("ReturnOfNull")
    public Integer getInteger() {
        return null;
    }

    @Override
    public String getString(final String arg) {
        return "NOT A GETTER";
    }

    @Override
    public Integer value() {
        return 3;
    }

    @Override
    public TestInterface getObject() {
        return new TestBean("justNotANoArgs");
    }
}
