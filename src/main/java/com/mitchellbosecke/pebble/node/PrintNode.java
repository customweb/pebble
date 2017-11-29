/*******************************************************************************
 * This file is part of Pebble.
 *
 * Copyright (c) 2014 by Mitchell Bösecke
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble.node;

import java.io.IOException;
import java.io.Writer;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class PrintNode extends AbstractRenderableNode {

	private Expression<?> expression;
	private final ObjectPrinter printer;

	public PrintNode(ObjectPrinter printer, Expression<?> expression, int lineNumber) {
		super(lineNumber);
		if (printer == null) {
			throw new IllegalStateException("The printer is required.");
		}
		this.expression = expression;
		this.printer = printer;
	}

	@Override
	public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context)
			throws IOException, PebbleException {
		Object var = expression.evaluate(self, context);
		if (var != null) {
			this.printer.print(var, writer);
		}
	}

	@Override
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}

	public Expression<?> getExpression() {
		return expression;
	}

	public void setExpression(Expression<?> expression) {
		this.expression = expression;
	}

}
