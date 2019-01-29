package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.List;

import arrayscript.lang.ASElement;
import arrayscript.lang.ASNamespace;
import arrayscript.parser.util.ParsingException;

public class NamespaceBuilder implements ElementBuilder {
	
	private final NamespaceBuilder parent;
	private final String name;
	
	private final List<ElementBuilder> elements;
	private final List<NamespaceBuilder> namespaces;
	
	/**
	 * Constructs a new empty namespace with the given name and parent. If both name and parent are null,
	 * this namespace will consider itself the global namespace.
	 * @param name The name of this namespace, or null if this is the global namespace
	 * @param parent The parent namespace of this namespace, or null if this is the global namespace
	 */
	public NamespaceBuilder(String name, NamespaceBuilder parent) {
		if ((name == null) != (parent == null)) {
			throw new IllegalArgumentException("name and parent must be both null or both not null");
		}
		this.name = name;
		this.parent = parent;
		this.elements = new ArrayList<ElementBuilder>(30);
		this.namespaces = new ArrayList<NamespaceBuilder>();
	}
	
	@Override
	public String toString() {
		if (parent == null) {
			return "global namespace";
		} else if (!parent.isGlobal()){
			return parent + "." + name;
		} else {
			return "namespace " + name;
		}
	}
	
	public ASNamespace build() {
		ASElement[] finishedElements = new ASElement[elements.size()];
		int index = 0;
		for (ElementBuilder builder : elements) {
			finishedElements[index++] = builder.build();
		}
		return new ASNamespace(name, finishedElements);
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * If this namespace does not contain an element with the given name, a new namespace will be added to
	 * this namespace and returned. If there already exists a namespace with the given name, it will be
	 * returned so that it can be expanded. If there exists a non-namespace element with the given name,
	 * a ParsingException will be thrown.
	 * @param name The name of the namespace to create or expand
	 * @return a possibly empty namespace that is ready to be expanded
	 * @throws ParsingException if there exists a non-namespace element with the given name
	 */
	public NamespaceBuilder createNamespace(String name) throws ParsingException {
		
		// If there is already a namespace with the given name, let it be expanded
		NamespaceBuilder namespace = getNamespace(name);
		if (namespace != null) {
			return namespace;
		}
		
		// If there is another type of element with the same name, forbid the operation
		if (hasElement(name)) {
			throw new ParsingException("Duplicated element '" + name + "' in namespace " + this);
		}
		
		// Create a new namespace
		namespace = new NamespaceBuilder(name, this);
		elements.add(namespace);
		namespaces.add(namespace);
		return namespace;
	}
	
	public boolean isGlobal() {
		return parent == null;
	}
	
	public boolean hasElement(String name) {
		for (ElementBuilder element : elements) {
			if (element.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public ElementBuilder getElement(String name) {
		for (ElementBuilder element : elements) {
			if (element.getName().equals(name)) {
				return element;
			}
		}
		return null;
	}
	
	public NamespaceBuilder getNamespace(String name) {
		for (NamespaceBuilder namespace : namespaces) {
			if (namespace.getName().equals(name)) {
				return namespace;
			}
		}
		return null;
	}
}