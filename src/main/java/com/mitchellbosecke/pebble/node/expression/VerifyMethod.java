package com.mitchellbosecke.pebble.node.expression;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Handles a separate list of safe and unsafe methods in regards to a high security issue that relates to
 * being able to instantiate an instance of an arbitrary class from within a template.
 */

class VerifyMethod {

    private static final String UNSAFE_METHODS_PROPERTIES = "/unsafeMethods.properties";
    private static final Set<Method> UNSAFE_METHODS = createUnsafeMethodsSet();

    VerifyMethod() { }

    //----------------------------------------------------------------------------------------------------------------//
    // TODO: Check for the unsafe exceptions, refactor, and reverse the logic for safe (whitelist) methods.
    // TODO: Remove the blacklist functionality and review for whitelist testing purposes.
    //---------------------------------------------------------------------------------------------------------------//

    // @Deprecated // Still used for testing
    boolean isUnsafeMethod(Method method) {
        return UNSAFE_METHODS.contains(method);
    }

    @Deprecated // Still used for testing
    private static Properties loadProperties(String resource) throws IOException {
        Properties props = new Properties();
        InputStream is = null;
        try {
            is = VerifyMethod.class.getResourceAsStream(resource);
            props.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return props;
    }

    @Deprecated
    private static final Set<Method> createUnsafeMethodsSet() {
        try {
            Properties props = loadProperties(UNSAFE_METHODS_PROPERTIES);
            Set<Method> set = new HashSet<>(props.size() * 4 / 3, 1f);
            Map<String, Class> primClasses = createPrimitiveClassesMap();
            for (Object key : props.keySet()) {
                set.add(parseMethodSpec((String) key, primClasses));
            }
            return set;
        } catch (Exception e) {
            throw new RuntimeException("Could not load unsafe method set", e);
        }
    }

    @Deprecated
    private static Method parseMethodSpec(String methodSpec, Map<String, Class> primClasses)
            throws ClassNotFoundException,
            NoSuchMethodException {
        int brace = methodSpec.indexOf('(');
        int dot = methodSpec.lastIndexOf('.', brace);
        Class clazz = Class.forName(methodSpec.substring(0, dot));
        String methodName = methodSpec.substring(dot + 1, brace);
        String argSpec = methodSpec.substring(brace + 1, methodSpec.length() - 1);
        StringTokenizer tok = new StringTokenizer(argSpec, ",");
        int argcount = tok.countTokens();
        Class[] argTypes = new Class[argcount];
        for (int i = 0; i < argcount; i++) {
            String argClassName = tok.nextToken();
            argTypes[i] = primClasses.get(argClassName);
            if (argTypes[i] == null) {
                argTypes[i] = Class.forName(argClassName);
            }
        }
        return clazz.getMethod(methodName, argTypes);
    }

    @Deprecated
    private static Map<String, Class> createPrimitiveClassesMap() {
        Map<String, Class> map = new HashMap<>();
        map.put("boolean", Boolean.TYPE);
        map.put("byte", Byte.TYPE);
        map.put("char", Character.TYPE);
        map.put("short", Short.TYPE);
        map.put("int", Integer.TYPE);
        map.put("long", Long.TYPE);
        map.put("float", Float.TYPE);
        map.put("double", Double.TYPE);
        return map;
    }

}
