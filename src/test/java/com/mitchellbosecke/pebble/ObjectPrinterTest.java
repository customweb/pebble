package com.mitchellbosecke.pebble;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.ObjectPrintStrategy;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.node.ObjectPrinter;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

/**
 * Tests {@link ObjectPrinter}
 *
 * @author Thomas Hunziker
 *
 */
public class ObjectPrinterTest {

	@Test
	public void fullTest() throws PebbleException, IOException {

		PebbleEngine pebble = new PebbleEngine.Builder().loader(new StringLoader()).strictVariables(false)
				.extension(new TestExtension()).build();

		PebbleTemplate template = pebble.getTemplate("{{ input }}");

		Map<String, Object> context = new HashMap<>();
		context.put("input", new SimpleType());
		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("expected", writer.toString());

	}

	@Test
	public void fullTestRaw() throws PebbleException, IOException {

		PebbleEngine pebble = new PebbleEngine.Builder().loader(new StringLoader()).strictVariables(false)
				.extension(new TestExtension()).build();

		PebbleTemplate template = pebble.getTemplate("{{ input | raw }}");

		Map<String, Object> context = new HashMap<>();
		context.put("input", new SimpleType());
		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("expected", writer.toString());

	}

	/**
	 * Tests if the strategy pattern works as expected.
	 *
	 * @throws IOException
	 */
	@Test
	public void simpleTest() throws IOException {
		Collection<SimplePrinter> printers = Collections.singleton(new SimplePrinter());
		ObjectPrinter printer = new ObjectPrinter(printers);

		StringWriter writer = new StringWriter();
		printer.print(new SimpleType(), writer);

		Assert.assertEquals("expected", writer.toString());

	}

	private static final class SimpleType {

		@Override
		public String toString() {
			return "not expected";
		}

	}

	private static final class SimplePrinter implements ObjectPrintStrategy {

		@Override
		public boolean supports(Class<?> objectType) {
			return SimpleType.class.isAssignableFrom(objectType);
		}

		@Override
		public void print(Object object, Writer writer) throws IOException {
			writer.write("expected");
		}

	}

	private static class TestExtension extends AbstractExtension {

		@Override
		public List<ObjectPrintStrategy> getObjectPrintStrategies() {
			List<ObjectPrintStrategy> strategies = new ArrayList<>();
			strategies.add(new SimplePrinter());
			return strategies;
		}
	}

}
