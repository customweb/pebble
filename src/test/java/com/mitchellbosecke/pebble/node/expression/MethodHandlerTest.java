package com.mitchellbosecke.pebble.node.expression;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.mitchellbosecke.pebble.utils.WhiteListObject;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class MethodHandlerTest {

    @Test
    public void testWhiteListAndWhiteListIntoConstructor() throws PebbleException, IOException, NoSuchMethodException {

        // Create Dummy WhiteList with Method
        Set<Method> whiteList = new HashSet<>();
        Method method = PebbleEngine.class.getMethod("getTemplate", String.class);
        whiteList.add(method);
        WhiteListObject whiteListObject = new WhiteListObject(whiteList);

        // Pass Properties into Builder
        PebbleEngine.Builder builder = new PebbleEngine.Builder();
        builder.loader(new StringLoader());
        builder.strictVariables(false);
        builder.whiteList(whiteListObject);
        PebbleEngine pebbleEngine = builder.build();

        // Set Up Test
        String source = "{% for user in users %}{% if loop.first %}[{{ loop.length }}]{% endif %}{% if loop.last %}[{{ loop.length }}]{% endif %}{{ loop.index }}{{ loop.revindex }}{{ user.username }}{% endfor %}";
        PebbleTemplate template = pebbleEngine.getTemplate(source);
        Map<String, Object> context = new HashMap<>();
        List<UserObject> users = new ArrayList<>();
        users.add(new UserObject("Alex"));
        users.add(new UserObject("Bob"));
        users.add(new UserObject("John"));
        context.put("users", users);
        Writer writer = new StringWriter();
        template.evaluate(writer, context);

        // Run Test
        assertEquals("[3]02Alex11Bob[3]20John", writer.toString());
        assertEquals(1, pebbleEngine.getWhiteListObject().getWhiteList().size());
    }

    public static class UserObject {
        private final String username;

        public UserObject(String username) {
            this.username = username;
        }
        public String getUsername() { return username; }
    }

}

