package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.util.Checks;

public class GetterBuilder implements ElementBuilder {
	
	private final String propertyName;
	private final String methodName;
	private final Set<Modifier> modifiers;
	
	private ExecutableBuilder body;
	
	/**
	 * Creates a GetterBuilder with the given property name and modifiers. This getter will not have a custom
	 * body.
	 * @param propertyName The name of the property of the getter
	 * @param modifiers The modifiers of the getter
	 */
	public GetterBuilder(String propertyName, Set<Modifier> modifiers) {
		Checks.notNull(propertyName);
		Checks.noNull(modifiers);
		this.propertyName = propertyName;
		this.modifiers = modifiers;
		this.methodName = "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
	}
	
	/**
	 * Creates a getter with the given property name, modifiers and body. The getter will thus have a custom
	 * body.
	 * @param propertyName The name of the property this getter is for
	 * @param modifiers The modifiers of the getter
	 * @param body The custom body of the getter
	 */
	public GetterBuilder(String propertyName, Set<Modifier> modifiers, ExecutableBuilder body) {
		this(propertyName, modifiers);
		Checks.notNull(body);
		this.body = body;
	}

	@Override
	public String getName() {
		return propertyName;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public boolean hasCustomBody() {
		return body != null;
	}
	
	public boolean isImplicit() {
		return modifiers.contains(Modifier.IMPLICIT);
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}