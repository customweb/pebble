package com.mitchellbosecke.pebble.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.mitchellbosecke.pebble.error.ClassAccessException;
import com.mitchellbosecke.pebble.error.PebbleException;

public final class WhiteListObject {

    private final Set<Method> whiteList;
    private final BlockingBehavior behavior;
    
    public static final BlockingBehavior DEFAULT_BEHAVIOR = new BlockingBehavior() {

		@Override
		public void onMethodBlock(Method method, int lineNumber, String fileName) throws PebbleException {
			throw new ClassAccessException(lineNumber, fileName);
		}
    	
    };
    
    
    public WhiteListObject(Collection<Method> whiteList) {
    	this(whiteList, DEFAULT_BEHAVIOR);
    }
 
    public WhiteListObject(Collection<Method> whiteList, BlockingBehavior behavior) {
        if (whiteList != null) {
        	this.whiteList = Collections.unmodifiableSet(new HashSet<>(whiteList));
         }
        else {
        	this.whiteList = new HashSet<>();
        }
        this.behavior = behavior;
    }
    
    public boolean isWhitelisted(Method method) {
    	return this.whiteList.contains(method);
    }
    
    public Set<Method> getWhiteList() {
        return whiteList;
    }

    public BlockingBehavior getBlockingBehavior() {
    	return this.behavior;
    }
    
    public interface BlockingBehavior {
    	
    	public void onMethodBlock(Method method, int lineNumber, String filename) throws PebbleException;
    	
    }
    
    
}
