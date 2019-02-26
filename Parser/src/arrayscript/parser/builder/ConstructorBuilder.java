package arrayscript.parser.builder;

import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.util.Checks;

public class ConstructorBuilder implements ElementBuilder {
	
	private final Set<Modifier> modifiers;
	private final ParamsBuilder params;
	private final List<SourceElement> head;
	private final ExecutableBuilder body;
	
	public ConstructorBuilder(Set<Modifier> modifiers, ParamsBuilder parameters, List<SourceElement> head, ExecutableBuilder body) {
		Checks.noNull(modifiers);
		Checks.notNull(parameters);
		Checks.noNull(head);
		Checks.notNull(body);
		this.modifiers = modifiers;
		this.params = parameters;
		this.head = head;
		this.body = body;
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("Constructors don't have names");
	}

	@Override
	public Element build() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	public ParamsBuilder getParameteters() {
		return params;
	}
	
	public List<SourceElement> getHead(){
		return head;
	}
	
	public ExecutableBuilder getBody() {
		return body;
	}
}