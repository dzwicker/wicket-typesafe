package com.github.dzwicker.wicket.typesafe;

public interface TestInterface {

    String getString();

    Integer getInteger();

    String getString(final String arg);

    Integer value();

    TestInterface getObject();
}
