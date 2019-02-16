package arrayscript.parser.builder.param;

import arrayscript.parser.builder.var.type.TypeBuilder;

public class ParamBuilder {
	
	private final String name;
	
	private final TypeBuilder type;
	
	public ParamBuilder(String typeName, String name) {
		this.name = name;
		this.type = new TypeBuilder(typeName);
	}
	
	public ParamBuilder(TypeBuilder type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public TypeBuilder getType() {
		return type;
	}
}