package com.github.dzwicker.wicket.typesafe;

public interface TypeSafeWicketId {

    String typeSafeWicketIdGenerate();

    void typeSafeWicketIdGenerate(StringBuilder builder);

    TypeSafeWicketId getParent();
}
