package com.mitchellbosecke.pebble.node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.mitchellbosecke.pebble.extension.ExtensionRegistry;
import com.mitchellbosecke.pebble.extension.ObjectPrintStrategy;
import com.mitchellbosecke.pebble.utils.StringUtils;

/**
 * The object printer provides the facility to print an arbitrary object to the
 * output writer. So whenever an object should be printed the printer can help
 * to do this.
 *
 * <p>
 * When an arbitrary object is printed in a template this may cause issues
 * because we will invoke normally the {@link Object#toString()} method. Which
 * may be implemented for other use cases in a different way or the objects are
 * provided by a third party and as such the toString method cannot be
 * overridden.
 *
 * <p>
 * The printer implements the strategy pattern which allows to define for the
 * different object types different printers. See also
 * {@link ObjectPrintStrategy}.
 *
 * @author Thomas Hunziker
 *
 */
public final class ObjectPrinter {

	private final List<ObjectPrintStrategy> objectPrintStrategies;
	private final ConcurrentMap<Class<?>, ObjectPrintStrategy> cache = new ConcurrentHashMap<>();
	private final DefaultPrinter defaultPrinter = new DefaultPrinter();

	public ObjectPrinter(ExtensionRegistry extensionRegistry) {
		this(extensionRegistry.getObjectPrintStrategies());
	}

	public ObjectPrinter(Collection<? extends ObjectPrintStrategy> objectPrintStrategies) {
		if (objectPrintStrategies == null) {
			throw new IllegalArgumentException("The provided objectPrintStrategies cannot be null.");
		}
		this.objectPrintStrategies = Collections.unmodifiableList(new ArrayList<>(objectPrintStrategies));
	}

	public String converToString(Object object) {
		StringWriter writer = new StringWriter();
		try {
			this.print(object, writer);
		} catch (IOException e) {
			throw new IllegalStateException("This should never happen.", e);
		}
		return writer.toString();
	}

	public void print(Object object, Writer writer) throws IOException {
		this.resolvePrinter(object).print(object, writer);
	}

	private ObjectPrintStrategy resolvePrinter(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("The object cannot be null.");
		}
		final Class<?> type = object.getClass();

		ObjectPrintStrategy printer = this.cache.get(type);
		if (printer == null) {
			printer = this.resolvePrinterInner(type);
			this.cache.putIfAbsent(type, printer);
		}

		return printer;
	}

	private ObjectPrintStrategy resolvePrinterInner(Class<?> type) {

		for (ObjectPrintStrategy p : objectPrintStrategies) {
			if (p.supports(type)) {
				return p;
			}
		}

		// When there is not printer defined we use our default printer which
		// uses the toString() method.
		return defaultPrinter;
	}

	private static class DefaultPrinter implements ObjectPrintStrategy {

		@Override
		public boolean supports(Class<?> objectType) {
			return true;
		}

		@Override
		public void print(Object object, Writer writer) throws IOException {
			writer.write(StringUtils.toString(object));
		}
	}

}
