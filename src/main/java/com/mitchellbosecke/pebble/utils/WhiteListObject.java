package com.mitchellbosecke.pebble.utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.*;

public class WhiteListObject {

    private final Set<Method> mutedWhitelist;
    private final JSONArray jsonArray;

    public Set<Method> getWhiteList() { return mutedWhitelist; }

    public WhiteListObject(JSONArray jsonArray){
    	// FIXME The jsonArray should not be an instance variable as it is going to be used once the constructor has finished its execution.
    	// --> I would really make a factory method for this.

        // Check Null
        Objects.requireNonNull(jsonArray, "The jsonArray can not be null.");

        // Make Immutable Params
        JSONObject jsonObject = new JSONObject().put("wrapper",jsonArray);
        this.jsonArray = jsonObject.getJSONArray("wrapper");

        // Execute conversion
        try {
            this.mutedWhitelist = parseJsonArrayToMethodSet();
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading Whitelist;", e);
        }
    }

    private Set<Method> parseJsonArrayToMethodSet() throws NoSuchMethodException, ClassNotFoundException {
        Set<Method> whitelist;
            Set<Method> set = new HashSet<>(jsonArray.length() * 4 / 3, 1f);
            Map<String, Class> primitiveClasses = getPrimitiveClasses();
            for(int i =0;i<jsonArray.length();i++){
                set.add(parseJsonArrayToMethodSetDetails(String.valueOf(jsonArray.get(i)), primitiveClasses));
            }
            whitelist = Collections.unmodifiableSet(set);
        return Collections.unmodifiableSet(whitelist);
    }

    private static Method parseJsonArrayToMethodSetDetails(String className, Map<String, Class> primitiveClasses)
            throws ClassNotFoundException, NoSuchMethodException {

        // Parse className into its components: ClassName.MethodName.etc(params)
        int openBracket = className.indexOf('(');
        int lastDotInString = className.lastIndexOf('.', openBracket);
        Class klass = Class.forName(className.substring(0, lastDotInString));
        String methodName = className.substring(lastDotInString + 1, openBracket);
        String argSpec = className.substring(openBracket + 1, className.length() - 1);
        StringTokenizer tokens = new StringTokenizer(argSpec, ",");
        int argumentCount = tokens.countTokens();
        Class[] argTypes = new Class[argumentCount];

        for (int i = 0; i < argumentCount; i++) {
            String argClassName = tokens.nextToken();
            argTypes[i] = primitiveClasses.get(argClassName);
            if (argTypes[i] == null) {
                argTypes[i] = Class.forName(argClassName);
            }
        }

        return klass.getMethod(methodName, argTypes);
    }

    private static Map<String, Class> getPrimitiveClasses() {
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
