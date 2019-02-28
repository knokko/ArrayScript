package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.util.Checks;

public class SetterBuilder implements ElementBuilder {
	
	private final String name;
	private final String methodName;
	private final Set<Modifier> modifiers;
	
	private String paramName;
	private ExecutableBuilder body;
	
	public SetterBuilder(String propertyName, Set<Modifier> modifiers) throws ParsingException {
		Checks.notNull(propertyName);
		Checks.noNull(modifiers);
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.SETTER.canHave(modifier)) {
				throw new ParsingException("Setters can't have the " + modifier + " modifier");
			}
		}
		this.name = propertyName;
		this.modifiers = modifiers;
		
		this.methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	
	public SetterBuilder(String propertyName, Set<Modifier> modifiers, String paramName, ExecutableBuilder body) throws ParsingException {
		this(propertyName, modifiers);
		Checks.notNull(paramName);
		Checks.notNull(body);
		this.body = body;
		this.paramName = paramName;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public String getPropertyName() {
		return name;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public boolean isDefault() {
		return body == null;
	}
	
	public boolean isCustom() {
		return body != null;
	}
	
	public ExecutableBuilder getBody() {
		if (body == null) {
			throw new UnsupportedOperationException("Setter " + name + " doesn't have a body");
		}
		return body;
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}