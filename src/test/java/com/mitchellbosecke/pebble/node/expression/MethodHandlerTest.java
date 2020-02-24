package com.mitchellbosecke.pebble.node.expression;

import com.google.common.cache.Cache;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.Extension;
import com.mitchellbosecke.pebble.loader.StringLoader;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MethodHandlerTest {

    @Test
    public void testToAddNewDynamicWhiteListPropertiesFile() {

        // The filename will be set dynamically from properties variable.
        String path = "whitelist1.properties";

        //Create Pebble Engine Instance
        PebbleEngine pebbleEngine = createEngine();

        // Create Dummy Whitelist with Identifier
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

    public PebbleEngine createEngine(){
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(new StringLoader());
        builder.extension(mock(Extension.class));
        builder.strictVariables(false);
        builder.templateCache(mock(Cache.class));
        return builder.build();
    }

}