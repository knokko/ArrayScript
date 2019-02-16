package arrayscript.parser.builder;

import java.util.Collection;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.util.ParsingException;

public class MainBuilder implements ElementBuilder {
	
	private final NamespaceBuilder namespace;
	private final String name;
	
	private final ExecutableBuilder body;
	
	public MainBuilder(NamespaceBuilder namespace, Collection<Modifier> modifiers, String name, ExecutableBuilder body) throws ParsingException {
		
		// Mains can't have modifiers
		if (modifiers != null && !modifiers.isEmpty()) {
			throw new ParsingException("Mains can't have modifiers, but " + name + " has modifiers " + modifiers);
		}
		
		this.namespace = namespace;
		this.name = name;
		this.body = body;
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