package com.github.dzwicker.wicket.typesafe;

import static com.github.dzwicker.wicket.typesafe.CreateId.id;
import static com.github.dzwicker.wicket.typesafe.CreateId.of;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("ClassWithTooManyMethods")
public class CreateIdTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldGenerateProxyWithNewInterfaceForInterface() {
        final TestInterface originalBean = new TestBean("justNotANoArgs");
        final TestInterface proxy = of(originalBean);

        assertThat(originalBean, not(instanceOf(TypeSafeWicketId.class)));
        assertThat(proxy, instanceOf(TypeSafeWicketId.class));
    }

    @Test
    public void shouldGenerateProxyWithNewInterfaceForClass() {
        final TestBean originalBean = new TestBean("justNotANoArgs");
        final TestBean proxy = of(originalBean);

        assertThat(originalBean, not(instanceOf(TypeSafeWicketId.class)));
        assertThat(proxy, instanceOf(TypeSafeWicketId.class));
    }

    @Test
    public void onlyGetterShouldBeHandheld() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("wicket-typesafe handles only getter calls!"));
        final TestBean originalBean = new TestBean("justNotANoArgs");
        of(originalBean).value();
    }

    @Test
    public void evenGetterWithArgsShouldNotBeHandheld() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("wicket-typesafe handles only getter calls!"));
        final TestBean originalBean = new TestBean("justNotANoArgs");
        of(originalBean).getString("test");
    }

    @Test
    public void shouldGenerateIdForSimpleGetter() {
        final TestBean originalBean = new TestBean("justNotANoArgs");
        final String generatedId = id(of(originalBean).getObject());

        assertThat(generatedId, equalTo("object"));
    }

    @Test
    public void shouldGenerateIdForSimpleGetterReturnFinalJDKClass() {
        final TestBean originalBean = new TestBean("justNotANoArgs");
        final String generatedId = id(of(originalBean).getInteger());

        assertThat(generatedId, equalTo("integer"));
    }

    @Test
    public void shouldGenerateIdForGetterWithGETInName() {
        final TestInterface of = of(TestInterface.class);
        final String generatedId1 = id(of.getTestgetGetter());
        final String generatedId2 = id(of.isTestgetGetter());

        assertThat(generatedId1, equalTo("testgetGetter"));
        assertThat(generatedId2, equalTo("testgetGetter"));
    }

    @Test
    public void shouldGenerateIdForGetterWithISInName() {
        final TestInterface of = of(TestInterface.class);
        final String generatedId1 = id(of.getTestisGetter());
        final String generatedId2 = id(of.isTestisGetter());

        assertThat(generatedId1, equalTo("testisGetter"));
        assertThat(generatedId2, equalTo("testisGetter"));
    }

    @Test
    public void shouldGenerateIdForRecursiveGetter() {
        final TestBean originalBean = new TestBean("justNotANoArgs");
        final String generatedId = id(of(originalBean).getObject().getObject());

        assertThat(generatedId, equalTo("object.object"));
    }

    @Test
    public void shouldGenerateIdForRecursiveGetterReturnFinalJDKClass() {
        final TestBean originalBean = new TestBean("justNotANoArgs");
        final String generatedId = id(of(originalBean).getObject().getInteger());

        assertThat(generatedId, equalTo("object.integer"));
    }

    @Test
    public void shouldGenerateIdsAfterRecursiveGetterReturnFinalJDKClass() {
        final TestInterface of = of(TestInterface.class);
        final String generatedId1 = id(of.getObject().getInteger());
        final String generatedId2 = id(of.getString());

        assertThat(generatedId1, equalTo("object.integer"));
        assertThat(generatedId2, equalTo("string"));
    }

    @Test
    public void shouldGenerateIdsAfterRecursiveGetter() {
        final TestInterface of = of(TestInterface.class);
        final String generatedId1 = id(of.getChild().getBean());
        final String generatedId2 = id(of.getBean());

        assertThat(generatedId1, equalTo("child.bean"));
        assertThat(generatedId2, equalTo("bean"));
    }

    @Test
    public void shouldGenerateProxyWithNewInterfaceForInterfaceOfClassObject() {
        final TestInterface proxy = of(TestInterface.class);

        assertThat(proxy, instanceOf(TypeSafeWicketId.class));
    }

    @Test
    public void shouldGenerateIdForSimpleGetterFromClassObject() {
        final String generatedId = id(of(TestBean.class).getObject());

        assertThat(generatedId, equalTo("object"));
    }

    @Test
    public void shouldGenerateIdForSimpleGetterFromInterface() {
        final String generatedId = id(of(TestInterface.class).getObject());

        assertThat(generatedId, equalTo("object"));
    }

    @Test
    public void shouldGenerateIdForSimpleGetterReturnFinalJDKClassForClassObject() {
        final String generatedId = id(of(TestBean.class).getInteger());

        assertThat(generatedId, equalTo("integer"));
    }

    @Test
    public void shouldGenerateIdForRecursiveGetterReturnFinalJDKClassForClassObject() {
        final String generatedId = id(of(TestBean.class).getObject().getInteger());

        assertThat(generatedId, equalTo("object.integer"));
    }

    @Test
    public void shouldGenerateIdForSimpleGetterReturnFinalJDKClassForInterface() {
        final String generatedId = id(of(TestInterface.class).getInteger());

        assertThat(generatedId, equalTo("integer"));
    }

    @Test
    public void shouldGenerateIdForRecursiveGetterReturnFinalJDKClassForInterface() {
        final String generatedId = id(of(TestInterface.class).getObject().getInteger());

        assertThat(generatedId, equalTo("object.integer"));
    }

}
