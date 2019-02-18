package arrayscript.parser.builder;

import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.util.ParsingException;

public class ClassBuilder implements ElementBuilder {
	
	private final String name;
	private final NamespaceBuilder namespace;
	private final Set<Modifier> modifiers;
	
	public ClassBuilder(String name, NamespaceBuilder namespace, Set<Modifier> modifiers) throws ParsingException {
		this.name = name;
		this.namespace = namespace;
		
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.CLASS.canHave(modifier)) {
				throw new ParsingException("Classes can't have the " + modifier + " modifier");
			}
		}
		this.modifiers = modifiers;
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