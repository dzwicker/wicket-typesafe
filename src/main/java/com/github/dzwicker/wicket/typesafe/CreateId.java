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

    private static final ThreadLocal<TypeSafeWicketId> RECORDERS = new ThreadLocal<>();

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
        RECORDERS.set((TypeSafeWicketId) proxy);
        return proxy;
    }

    public static String id(final Object obj) {
        final TypeSafeWicketId typeSafeWicketId;
        if (obj instanceof TypeSafeWicketId) {
            typeSafeWicketId = (TypeSafeWicketId) obj;
        } else {
            typeSafeWicketId = RECORDERS.get();
        }
        return typeSafeWicketId.typeSafeWicketIdGenerate();
    }

}
