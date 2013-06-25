package com.github.dzwicker.wicket.typesafe;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * This method interceptor loops through the delegate objects first. If it finds an
 * objects which implements an interface that matches the intercepted method signature,
 * then it will run the method on this delegate. If no matching delegate is found, then
 * the superclass methods are called.
 */
public class InterfaceMethodInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InterfaceMethodInterceptor.class);

    private static final Pattern GET = Pattern.compile("get");
    private static final Pattern IS = Pattern.compile("is");
    private static final Method generateMethod;
    private static final Method parentGenerateMethod;

    static {
        try {
            generateMethod = CreateId.TYPE_SAFE_WICKET_ID_INTERFACE.getMethod("typeSafeWicketIdGenerate");
            parentGenerateMethod = CreateId.TYPE_SAFE_WICKET_ID_INTERFACE.getMethod("typeSafeWicketIdGenerate",
                    StringBuilder.class);
        } catch (NoSuchMethodException e) {
            log.error("Unable to initialize wicket-typesafe. This is a bug please report it.", e);
            throw new RuntimeException(e);
        }
    }

    private final TypeSafeWicketId parent;

    private String recordedGetter;

    InterfaceMethodInterceptor(final TypeSafeWicketId parent) {
        this.parent = parent;
    }

    @Override
    public Object intercept(final Object self, final Method method, final Object[] args,
            final MethodProxy methodProxy)
            throws Throwable {

        if (method.equals(generateMethod)) {
            final StringBuilder builder = new StringBuilder();
            ((TypeSafeWicketId) self).typeSafeWicketIdGenerate(builder);
            return builder.toString();
        }

        if (method.equals(parentGenerateMethod)) {
            final StringBuilder builder = (StringBuilder) args[0];
            if (parent != null) {
                parent.typeSafeWicketIdGenerate(builder);
            }
            if (recordedGetter != null && parent != null) {
                builder.append(".");
            }
            if (recordedGetter != null) {
                builder.append(recordedGetter);
            }
            //noinspection ReturnOfNull
            return null;
        }

        recordGetterCall(method);

        final Class<?> returnType = method.getReturnType();
        if (Modifier.isFinal(returnType.getModifiers())) {
            /**
             * We can return null even if a NPE can happen.
             * NPE is ok because we are unable to record any further.
             */
            //noinspection ReturnOfNull
            return null;
        }

        return CreateId.of(returnType, (TypeSafeWicketId) self);
    }

    private void recordGetterCall(final Method method) {
        if (isGetter(method)) {
            String name = method.getName();
            name = GET.matcher(name).replaceFirst("");
            name = IS.matcher(name).replaceFirst("");
            recordedGetter = StringUtils.uncapitalize(name);
        } else {
            throw new IllegalArgumentException("wicket-typesafe handles only getter calls!");
        }
    }

    private boolean isGetter(final Method method) {
        if (!method.getName().startsWith("get")
                && !method.getName().startsWith("is")) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        if (void.class.equals(method.getReturnType())) {
            return false;
        }
        return true;
    }

}
