package com.mitchellbosecke.pebble.node.expression;

import com.google.common.cache.Cache;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.loader.Loader;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MethodHandlerTest {

    @Test
    public void testToCreateNewWhiteList() {

        // Pass Properties into Constructor Builder
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(mock(Loader.class));
        builder.extension(mock(Extension.class));
        builder.strictVariables(true);
        builder.templateCache(mock(Cache.class));
        PebbleEngine pebbleEngine = builder.build();

        // Create Dummy Whitelist with Identifier
        String path = "whitelist1.properties"; // Filename will be set from properties variable.
        Properties whitelist = new Properties();
        whitelist.put("fileType", path);

        // Add a dynamic whitelist post-construction
        pebbleEngine.addWhitelist(whitelist);

        // Check the whitelist exists in directory
        assertTrue(Files.exists(Paths.get(path)));

        // Cleanup
        File f = new File(path);
        assertTrue(f.delete());
    }

    @Test
    public void testWhitelistLogic(){

    }

}