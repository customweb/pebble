package com.mitchellbosecke.pebble.node.expression;

import com.google.common.cache.Cache;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.loader.Loader;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MethodHandlerTest {

    @Test
    public void testToCreateNewWhiteList() throws NoSuchMethodException {

        // Create Dummy WhiteList with Identifier
        Set<Method> whiteList = new HashSet<>();
        Method method = PebbleEngine.class.getMethod("getTemplate", String.class);
        whiteList.add(method);
        whiteList = Collections.unmodifiableSet(whiteList);

        // Pass Properties into Constructor Builder
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(mock(Loader.class));
        builder.extension(mock(Extension.class));
        builder.strictVariables(true);
        builder.templateCache(mock(Cache.class));
        builder.whiteList(whiteList);
        PebbleEngine pebbleEngine = builder.build();

        // Assert WhiteList
        assertTrue(pebbleEngine.getWhiteList().size() == 1);

    }

}