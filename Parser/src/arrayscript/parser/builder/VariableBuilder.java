package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.lang.element.Variable;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.util.Checks;

public class VariableBuilder implements ElementBuilder {
	
	private final Set<Modifier> modifiers;
	private final TypeBuilder type;
	private final ValueBuilder initialValue;
	
	private final String name;
	
	
	/**
	 * Creates a new variable builder that already has some information about its type and initial value. They
	 * are not necessarily complete already and they should be finished in a later parsing stage if they are
	 * not yet complete.
	 * @param modifiers The modifiers that the variable should get
	 * @param type The type builder that contains the current information about the type
	 * @param name The name of the variable, as declared in the source code
	 * @param initialValue The value builder that contains the current information about the initial value.
	 */
	public VariableBuilder(Set<Modifier> modifiers, TypeBuilder type, String name, ValueBuilder initialValue) throws ParsingException{
		Checks.noNull(modifiers);
		Checks.notNull(type, "type");
		Checks.notNull(name, "name");
		Checks.notNull(initialValue, "initialValue");
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.VARIABLE.canHave(modifier)) {
				throw new ParsingException("Variables can't have the " + modifier + " modifier");
			}
		}
		this.modifiers = modifiers;
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
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public TypeBuilder getType() {
		return type;
	}
	
	public ValueBuilder getInitialValue() {
		return initialValue;
	}
}