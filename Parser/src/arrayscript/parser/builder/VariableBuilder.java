package arrayscript.parser.builder;

import arrayscript.lang.element.Element;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.util.Checks;

public class VariableBuilder implements ElementBuilder {
	
	private final TypeBuilder type;
	
	private final String name;
	
	public VariableBuilder(String typeName, String name) {
		Checks.notNull(typeName, "typeName");
		Checks.notNull(name, "name");
		this.type = new TypeBuilder(typeName);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Element build() {
		if (!type.isTypeConfirmed()) {
			throw new IllegalStateException("Type has not yet been confirmed");
		}
		return null;
	}
}