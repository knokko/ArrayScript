package arrayscript.parser.builder;

import java.util.Collections;

import arrayscript.lang.Application;
import arrayscript.parser.util.ParsingException;

public class AppBuilder {
	
	private final NamespaceBuilder globalNamespace;
	
	public AppBuilder() {
		try {
			globalNamespace = new NamespaceBuilder(null, Collections.emptySet(), null);
		} catch (ParsingException e) {
			throw new Error("Should not be possible", e);
		}
	}
	
	public Application build() {
		return new Application(globalNamespace.build());
	}
	
	public NamespaceBuilder getGlobalNamespace() {
		return globalNamespace;
	}
}