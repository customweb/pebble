package com.mitchellbosecke.pebble.node.expression;

import com.google.common.cache.Cache;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.loader.Loader;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class VerifyMethodTest {

    @Test
    public void testPassingPropertiesIntoPebbleLibraryConstructor()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        // Variables
        Class unsafeMethods = VerifyMethod.class;
        String pathField = "UNSAFE_METHODS_PROPERTIES";
        String loadPropertiesMethod = "loadProperties";

        // Unlock and test private class
        Field path_field = unsafeMethods.getDeclaredField(pathField);
        path_field.setAccessible(true);
        String filePath = (String) path_field.get(pathField);
        assertFalse(filePath.isEmpty());

        // Unlock and test private class
        Method path_method = VerifyMethod.class.getDeclaredMethod(loadPropertiesMethod, String.class);
        path_method.setAccessible(true);
        Properties properties = (Properties) path_method.invoke(loadPropertiesMethod, filePath);
        assertTrue(properties.size() > 0);

        // Construct Builder with existing whitelist functionality
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(mock(Loader.class));
        builder.extension(mock(Extension.class));
        builder.strictVariables(true);
        builder.templateCache(mock(Cache.class));
        builder.whitelist(properties);
        PebbleEngine pebbleEngine = builder.build();
        assertTrue(pebbleEngine.getWhitelist().size() > 0);
    }

}