package arrayscript.parser.builder;

import arrayscript.lang.element.Element;

public class ClassBuilder implements ElementBuilder {
	
	private String name;
	private NamespaceBuilder namespace;
	
	public ClassBuilder(String name, NamespaceBuilder namespace) {
		this.name = name;
		this.namespace = namespace;
	}
	
	@Override
	public String toString() {
		if (namespace.isGlobal()) {
			return "class " + name;
		} else {
			return "class " + namespace + "." + name;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
}