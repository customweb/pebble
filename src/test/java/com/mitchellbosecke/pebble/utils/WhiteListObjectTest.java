package com.mitchellbosecke.pebble.utils;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.json.JSONArray;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WhiteListObjectTest {

    @Test
    public void testBuild() throws PebbleException, IOException {

        // Set Up JSON array
        JSONArray jsonArray = createJsonArray();

        // Set up Pebble Engine
        PebbleEngine pebbleEngine = createPebbleEngine(jsonArray);

        // Create Pebble Template
        String source = "{% for user in users %}{% if loop.first %}[{{ loop.length }}]{% endif %}{% if loop.last %}" +
                "[{{ loop.length }}]{% endif %}{{ loop.index }}{{ loop.revindex }}{{ user.username }}{% endfor %}";
        PebbleTemplate template = pebbleEngine.getTemplate(source);
        List<UserObject> users = getUserList();
        Map<String, Object> context = new HashMap<>();
        context.put("users", users);
        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        // Run Test
        assertEquals("[3]02Alex11Bob[3]20John", writer.toString());
        Set<Method> whiteList = pebbleEngine.getWhiteListObject().getWhiteList();

        // Assert Input and Output Length is equal
        assertEquals(jsonArray.length(), whiteList.size());

        //Assert Input and Output value is equal
        String y = jsonArray.getString(0);
        assertTrue(ObjectMethodArrayContains(whiteList.toArray(),y));

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Test Related Methods
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class UserObject {
        private final String username;

        public UserObject(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public JSONArray createJsonArray() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("java.lang.Object.notify()");
        jsonArray.put("java.lang.Object.notifyAll()");
        jsonArray.put("java.lang.Object.getClass()");
        jsonArray.put("java.lang.reflect.AccessibleObject.setAccessible([Ljava.lang.reflect.AccessibleObject;,boolean)");
        return jsonArray;
    }

    public PebbleEngine createPebbleEngine(JSONArray jsonArray) {
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(new StringLoader());
        builder.strictVariables(false);
        builder.whiteList(jsonArray);
        return builder.build();
    }

    private List<UserObject> getUserList() {
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Alex"));
        users.add(new UserObject("Bob"));
        users.add(new UserObject("John"));
        return users;
    }

    public boolean ObjectMethodArrayContains(Object[] arr, String targetValue) {
        for (Object s : arr) {
            if (s.toString().contains(targetValue))
                return true;
        }
        return false;
    }

}

