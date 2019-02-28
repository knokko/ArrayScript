package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.util.ParsingException;
import arrayscript.util.Checks;

public class MethodBuilder implements ElementBuilder {
	
	private final String name;
	private final TypeBuilder returnType;
	private final ParamsBuilder params;
	private final ExecutableBuilder body;
	private final Set<Modifier> modifiers;
	
	public MethodBuilder(String name, TypeBuilder returnType, ParamsBuilder params, ExecutableBuilder body, Set<Modifier> modifiers) throws ParsingException {
		Checks.notNull(name);
		Checks.notNull(params);
		Checks.notNull(body);
		Checks.noNull(modifiers);
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.METHOD.canHave(modifier)) {
				throw new ParsingException("Methods can't have the " + modifier + " modifier");
			}
		}
		this.name = name;
		this.returnType = returnType;
		this.params = params;
		this.body = body;
		this.modifiers = modifiers;
	}
	
	@Override
	public String toString() {
		return modifiers + " " + returnType + " " + name + "(...){...}";
	}

	@Override
	public String getName() {
		return name;
	}
	
	public boolean hasReturnType() {
		return returnType != null;
	}
	
	/**
	 * @return The (possibly unfinished) return type if this method has a return type
	 * @throws UnsupportedOperationException If this method doesn't have a return type
	 */
	public TypeBuilder getReturnType() throws UnsupportedOperationException {
		if (returnType == null) {
			throw new UnsupportedOperationException("The method " + name + " doesn't have a return type");
		} else {
			return returnType;
		}
	}
	
	/**
	 * Gets a 'nice' string representation of the return type of this method. If this method has a return type
	 * , it's readable name will be returned. If this method doesn't have a return type, "void" will be
	 * returned.
	 * @return A string representation of the return type of this method
	 */
	public String getReturnTypeName() {
		if (returnType == null) {
			return "void";
		} else {
			return returnType.getReadableTypeName();
		}
	}
	
	public ParamsBuilder getParameters() {
		return params;
	}
	
	public ExecutableBuilder getBody() {
		return body;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}