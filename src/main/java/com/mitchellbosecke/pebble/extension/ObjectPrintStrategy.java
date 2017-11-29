package com.mitchellbosecke.pebble.extension;

import java.io.IOException;
import java.io.Writer;

import com.mitchellbosecke.pebble.node.PrintNode;

/**
 * An object print strategy helps to convert an object into a string for
 * printing. So it is used within {@link PrintNode} to convert an arbitrary
 * object into a string which can be passed to the writer.
 *
 * <p>
 * This type of extension helps to convert custom objects into a good string
 * representation without requiring the user to specify within the template an
 * explicit filter.
 *
 * <p>
 * In case there is no object printer registred for a particular object type we
 * simply use {@link Object#toString()}.
 *
 * @author Thomas Hunziker
 *
 */
public interface ObjectPrintStrategy {

	/**
	 * This method determines if this object printer should be applied on the
	 * given {@code objectType}.
	 *
	 * <p>
	 * We use here the object type so that we can cache the resolved printer for
	 * a particular {@code objectType}. This way we do not need to call this
	 * method for each any every object.
	 *
	 * @param objectType
	 *            the type which should be checked.
	 * @return {@code true} when this printer is able to convert the given
	 *         object into a string.
	 */
	public boolean supports(Class<?> objectType);

	/**
	 * This method converts the given {@code object} into a string
	 * representation. This method is only called when the
	 * {@link #supports(Class)} method has returned {@code true} for the given
	 * object type.
	 *
	 * @param object
	 *            the object which should be converted.
	 * @param writer
	 *            the writer to which the {@code object} should be written to.
	 */
	public void print(Object object, Writer writer) throws IOException;

}
