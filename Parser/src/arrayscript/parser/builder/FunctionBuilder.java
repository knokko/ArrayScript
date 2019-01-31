package arrayscript.parser.builder;

import arrayscript.lang.element.Element;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;

public class FunctionBuilder implements ElementBuilder {
	
	private final String name;
	
	private final TypeBuilder returnType;
	
	private final ParamsBuilder parameters;
	
	public FunctionBuilder(String name, TypeBuilder returnType, ParamsBuilder parameters) {
		this.name = name;
		this.returnType = returnType;
		this.parameters = parameters;
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
	
	public boolean isConfirmed() {
		return returnType.isTypeConfirmed() && parameters.isConfirmed();
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}