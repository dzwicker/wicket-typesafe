package com.github.dzwicker.wicket.typesafe;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.objenesis.ObjenesisHelper;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

public class CreateId {

    static final Class<TypeSafeWicketId> TYPE_SAFE_WICKET_ID_INTERFACE = TypeSafeWicketId.class;
    private static final Class<?>[] EMPTY = {};

    private static final ThreadLocal<TypeSafeWicketId> ROOT_RECORDER = new ThreadLocal<>();
    private static final ThreadLocal<TypeSafeWicketId> LAST_RECORDER = new ThreadLocal<>();

    private CreateId() {
    }

    public static <T> T of(final Class<T> originalBeanClass) {
        return of(originalBeanClass, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T of(final T originalBean) {
        final Class<T> originalBeanClass = (Class<T>) originalBean.getClass();
        return of(originalBeanClass, null);
    }

    @SuppressWarnings("unchecked")
    static <T> T of(final Class<?> originalBeanClass, final TypeSafeWicketId parent) {
        final Enhancer enhancer = new Enhancer();
        final List<Class<?>> interfaces = new ArrayList<>();
        if (Modifier.isInterface(originalBeanClass.getModifiers())) {
            interfaces.add(originalBeanClass);
        } else {
            enhancer.setSuperclass(originalBeanClass);
        }
        interfaces.add(TYPE_SAFE_WICKET_ID_INTERFACE);

        final Class<?>[] interfacesAsArray = interfaces.toArray(EMPTY);
        enhancer.setInterfaces(interfacesAsArray);
        enhancer.setCallbackType(InterfaceMethodInterceptor.class);

        final Class<?> proxyClass = enhancer.createClass();
        Enhancer.registerCallbacks(proxyClass, new Callback[] {new InterfaceMethodInterceptor(parent)});

        final T proxy = (T) ObjenesisHelper.newInstance(proxyClass);
        saveProxy((TypeSafeWicketId) proxy);
        return proxy;
    }

    private static <T> void saveProxy(final TypeSafeWicketId typeSafeWicketId) {
        if (typeSafeWicketId.getParent() == null) {
            ROOT_RECORDER.set(typeSafeWicketId);
        }
        LAST_RECORDER.set(typeSafeWicketId);
    }

    public static String id(final Object obj) {
        final TypeSafeWicketId typeSafeWicketId;
        if (obj instanceof TypeSafeWicketId) {
            typeSafeWicketId = (TypeSafeWicketId) obj;
        } else {
            typeSafeWicketId = LAST_RECORDER.get();
        }
        final String generatedId = typeSafeWicketId.typeSafeWicketIdGenerate();
        resetRecorder();
        return generatedId;
    }

    private static void resetRecorder() {
        LAST_RECORDER.set(ROOT_RECORDER.get());
    }

}
