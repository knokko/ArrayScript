package arrayscript.parser.builder;

import java.util.List;

import arrayscript.lang.element.Element;
import arrayscript.lang.element.Variable;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.util.Checks;

public class VariableBuilder implements ElementBuilder {
	
	private final TypeBuilder type;
	private final ValueBuilder initialValue;
	
	private final String name;
	
	/**
	 * Creates a new variable builder with yet unknown type and initial value. The type and the initial value
	 * should be parsed in a later parsing stage.
	 * @param typeName The name of the type of this variable, as declared in the source code
	 * @param name The name of this variable, as declared in the source code
	 * @param unparsedValue The initial value that this variable should get, that is anything between the '='
	 * and the ';' that terminates the variable declaration.
	 */
	public VariableBuilder(String[] typeName, String name, List<SourceElement> unparsedValue) {
		Checks.notNull(typeName, "typeName");
		Checks.notNull(name, "name");
		this.type = new TypeBuilder(typeName);
		this.name = name;
		this.initialValue = new ValueBuilder(unparsedValue);
	}
	
	/**
	 * Creates a new variable builder that already has some information about its type and initial value. They
	 * are not necessarily complete already and they should be finished in a later parsing stage if they are
	 * not yet complete.
	 * @param type The type builder that contains the current information about the type
	 * @param name The name of the variable, as declared in the source code
	 * @param initialValue The value builder that contains the current information about the initial value.
	 */
	public VariableBuilder(TypeBuilder type, String name, ValueBuilder initialValue) {
		Checks.notNull(type, "type");
		Checks.notNull(name, "name");
		Checks.notNull(initialValue, "initialValue");
		this.type = type;
		this.name = name;
		this.initialValue = initialValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Element build() {
		return new Variable(name, type.getType(), initialValue.getValue());
	}
	
	public TypeBuilder getType() {
		return type;
	}
	
	public ValueBuilder getInitialValue() {
		return initialValue;
	}
}