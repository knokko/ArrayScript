package arrayscript.parser.builder;

import arrayscript.lang.element.Element;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.executable.ExecutableBuilder;

public class FunctionBuilder implements ElementBuilder {
	
	private final String name;
	
	private final TypeBuilder returnType;
	
	private final ParamsBuilder parameters;
	
	private final ExecutableBuilder body;
	
	public FunctionBuilder(String name, TypeBuilder returnType, ParamsBuilder parameters, ExecutableBuilder body) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
		this.body = body;
	}
	
	public String getName() {
		return name;
	}
	
	public TypeBuilder getReturnType() {
		return returnType;
	}
	
	public ParamsBuilder getParameters() {
		return parameters;
	}
	
	public ExecutableBuilder getBody() {
		return body;
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