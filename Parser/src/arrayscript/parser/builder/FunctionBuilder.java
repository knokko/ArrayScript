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

public class FunctionBuilder implements ElementBuilder {
	
	private final String name;
	
	private final Set<Modifier> modifiers;
	
	private final TypeBuilder returnType;
	
	private final ParamsBuilder parameters;
	
	private final ExecutableBuilder body;
	
	public FunctionBuilder(String name, Set<Modifier> modifiers, TypeBuilder returnType, ParamsBuilder parameters, ExecutableBuilder body) throws ParsingException {
		Checks.noNull(modifiers);
		Checks.notNull(parameters);
		Checks.notNull(body);
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.FUNCTION.canHave(modifier)) {
				throw new ParsingException("Functions can't have the " + modifier + " modifier");
			}
		}
		this.name = name;
		this.modifiers = modifiers;
		this.returnType = returnType;
		this.parameters = parameters;
		this.body = body;
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public TypeBuilder getReturnType() {
		if (returnType == null) {
			throw new UnsupportedOperationException("This function doesn't have a return type");
		}
		return returnType;
	}
	
	public ParamsBuilder getParameters() {
		return parameters;
	}
	
	public ExecutableBuilder getBody() {
		return body;
	}
	
	public String getReturnTypeName() {
		if (returnType == null) {
			return "void";
		} else {
			return returnType.getReadableTypeName();
		}
	}
	
	public boolean isConfirmed() {
		return returnType.isTypeConfirmed() && parameters.isConfirmed() && body.isConfirmed();
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}