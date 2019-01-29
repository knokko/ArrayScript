package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.List;

import arrayscript.lang.ASElement;
import arrayscript.lang.ASNamespace;

public class NamespaceBuilder implements ElementBuilder {
	
	private final String name;
	
	private final List<ElementBuilder> elements;
	private final List<NamespaceBuilder> namespaces;
	
	public NamespaceBuilder(String name) {
		this.name = name;
		this.elements = new ArrayList<ElementBuilder>(30);
		this.namespaces = new ArrayList<NamespaceBuilder>();
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
	
	public void addNamespace(NamespaceBuilder namespace) {
		elements.add(namespace);
		namespaces.add(namespace);
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