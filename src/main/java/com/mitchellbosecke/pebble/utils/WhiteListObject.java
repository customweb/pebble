package com.mitchellbosecke.pebble.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WhiteListObject {

    private final Set<Method> mutedWhitelist;
    public Set<Method> getWhiteList() {
        return mutedWhitelist;
    }
    // public WhiteListObject setNewWhiteList(Set<Method> newWhiteList) { return new WhiteListObject(newWhiteList); }

    // Constructor
    public WhiteListObject(Set<Method> whiteList) {
        Set<Method> nonNullSetList = new HashSet<>(Objects.requireNonNull(whiteList, "The white list cannot be null."));
        this.mutedWhitelist = Collections.unmodifiableSet(nonNullSetList);
    }

}
