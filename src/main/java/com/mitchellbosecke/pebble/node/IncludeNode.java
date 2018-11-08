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
import java.util.Collections;
import java.util.Map;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.extension.NodeVisitor;
import com.mitchellbosecke.pebble.node.expression.ContextVariableExpression;
import com.mitchellbosecke.pebble.node.expression.Expression;
import com.mitchellbosecke.pebble.node.expression.MapExpression;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplateImpl;

public class IncludeNode extends AbstractRenderableNode {

    private final Expression<?> includeExpression;

    private final MapExpression mapExpression;
    
    private final ContextVariableExpression variableExpression;

    public IncludeNode(int lineNumber, Expression<?> includeExpression, MapExpression mapExpression) {
        super(lineNumber);
        this.includeExpression = includeExpression;
        this.mapExpression = mapExpression;
        this.variableExpression = null;
    }
    
    public IncludeNode(int lineNumber, Expression<?> includeExpression, ContextVariableExpression variableExpression) {
        super(lineNumber);
        this.includeExpression = includeExpression;
        this.mapExpression = null;
        this.variableExpression = variableExpression;
    }

    @Override
    public void render(PebbleTemplateImpl self, Writer writer, EvaluationContext context) throws PebbleException,
            IOException {
        Object templateName = includeExpression.evaluate(self, context);

        if (!(templateName instanceof String)) {
            throw new PebbleException(null, "The name of the included template must be a string.", getLineNumber(), self.getName());
        }

        Map<?, ?> map = Collections.emptyMap();
        if (this.mapExpression != null) {
            map = this.mapExpression.evaluate(self, context);
        } else if (this.variableExpression != null) {
        	Object variable = this.variableExpression.evaluate(self, context);
        	if (variable instanceof Map<?, ?>) {
        		map = (Map<?, ?>) variable;
        	} else {
        		throw new PebbleException(null, "The include parameter needs to be a map.", getLineNumber(), self.getName());
        	}
        }

        if (templateName == null) {
            throw new PebbleException(
                    null,
                    "The template name in an include tag evaluated to NULL. If the template name is static, make sure to wrap it in quotes.",
                    getLineNumber(), self.getName());
        }
        self.includeTemplate(writer, context,  (String) templateName, map);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public Expression<?> getIncludeExpression() {
        return includeExpression;
    }

}
