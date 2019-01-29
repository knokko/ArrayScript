package arrayscript.parser.builder;

import arrayscript.lang.ASApplication;

public class AppBuilder {
	
	private final NamespaceBuilder globalNamespace;
	
	public AppBuilder() {
		globalNamespace = new NamespaceBuilder(null, null);
	}
	
	public ASApplication build() {
		return new ASApplication(globalNamespace.build());
	}
	
	public NamespaceBuilder getGlobalNamespace() {
		return globalNamespace;
	}
}