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

    @Override
    public String getTestgetGetter() {
        return "test";
    }

    @Override
    public String getTestisGetter() {
        return "test";
    }

    @Override
    public boolean isTestgetGetter() {
        return false;
    }

    @Override
    public boolean isTestisGetter() {
        return false;
    }

    @Override
    public TestInterface getChild() {
        return this;
    }

    @Override
    public TestInterface getBean() {
        return this;
    }
}
