package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.util.Checks;

public class PropertyBuilder implements ElementBuilder {
	
	private final String name;
	private final TypeBuilder type;
	private ValueBuilder defaultValue;
	
	private final Set<Modifier> modifiers;
	
	/**
	 * Creates a new property builder without default value. This means that all constructors must choose a
	 * value for this property.
	 * @param name The name of the property
	 * @param type The (possibly unfinished) type of the property
	 * @param modifiers The modifiers of the property
	 * @throws ParsingException If this property can't have all of the given modifiers
	 */
	public PropertyBuilder(String name, TypeBuilder type, Set<Modifier> modifiers) throws ParsingException {
		Checks.notNull(name);
		Checks.notNull(type);
		Checks.noNull(modifiers);
		
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.PROPERTY.canHave(modifier)) {
				throw new ParsingException("Properties can't have the " + modifier + " modifier");
			}
		}
		this.name = name;
		this.type = type;
		this.modifiers = modifiers;
	}
	
	public PropertyBuilder(String name, TypeBuilder type, Set<Modifier> modifiers, ValueBuilder defaultValue) throws ParsingException {
		this(name, type, modifiers);
		Checks.notNull(defaultValue);
		this.defaultValue = defaultValue;
	}
	
	@Override
	public String toString() {
		if (hasDefaultValue()) {
			return modifiers + " " + type + " " + name + " " + defaultValue;
		} else {
			return modifiers + " " + type + " " + name;
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean hasDefaultValue() {
		return defaultValue != null;
	}
	
	public TypeBuilder getType() {
		return type;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public ValueBuilder getDefaultValue() throws UnsupportedOperationException {
		if (defaultValue == null) {
			throw new UnsupportedOperationException("This property doesn't have a default value");
		}
		return defaultValue;
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}