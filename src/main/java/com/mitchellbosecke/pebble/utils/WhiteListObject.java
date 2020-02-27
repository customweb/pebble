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
    // Future Scope:
    // public WhiteListObject setNewWhiteList(Set<Method> newWhiteList) { return new WhiteListObject(newWhiteList); }

    // Constructor
    public WhiteListObject(Set<Method> whiteList) {
        // Future Scope:
        // String nullResponseMessage = "WhiteList must not be null"; // Exception optional
        //Set<Method> nonNullSetList = new HashSet<>(Objects.requireNonNull(whiteList, nullResponseMessage));
        Set<Method> nonNullSetList = new HashSet<>(Objects.requireNonNull(whiteList));
        this.mutedWhitelist = Collections.unmodifiableSet(nonNullSetList);
    }

}
