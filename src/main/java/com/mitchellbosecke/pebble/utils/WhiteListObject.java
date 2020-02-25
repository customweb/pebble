package com.mitchellbosecke.pebble.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class WhiteListObject {

    private final Set<Method> whiteList;

    public Set<Method> getWhiteList() {
        return whiteList;
    }

    public WhiteListObject(Set<Method> whiteList) {
        this.whiteList = Collections.unmodifiableSet(whiteList);
    }
}
