package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.Namespace;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.util.ParsingException;

public class NamespaceBuilder implements ElementBuilder {
	
	private final NamespaceBuilder parent;
	private final String name;
	
	private final Set<Modifier> modifiers;
	
	private final List<ElementBuilder> elements;
	
	private final List<NamespaceBuilder> namespaces;
	private final List<ClassBuilder> classes;
	private final List<VariableBuilder> variables;
	
	/**
	 * Constructs a new empty namespace with the given name and parent. If both name and parent are null,
	 * this namespace will consider itself the global namespace.
	 * @param name The name of this namespace, or null if this is the global namespace
	 * @param parent The parent namespace of this namespace, or null if this is the global namespace
	 */
	public NamespaceBuilder(String name, Set<Modifier> modifiers, NamespaceBuilder parent) throws ParsingException {
		if (!(name == null && parent == null && modifiers.isEmpty()) && !(name != null && parent != null)) {
			throw new IllegalArgumentException("Global namespace has no name, parent and modifiers and normal namespaces must have a name and parent.");
		}
		this.name = name;
		this.parent = parent;
		
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.NAMESPACE.canHave(modifier)) {
				throw new ParsingException("Namespaces can't have the " + modifier + " modifier, but " + name + " has it.");
			}
		}
		this.modifiers = modifiers;
		this.elements = new ArrayList<ElementBuilder>(30);
		this.namespaces = new ArrayList<NamespaceBuilder>();
		this.classes = new ArrayList<ClassBuilder>();
		this.variables = new ArrayList<VariableBuilder>();
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
	
	public Namespace build() {
		Element[] finishedElements = new Element[elements.size()];
		int index = 0;
		for (ElementBuilder builder : elements) {
			finishedElements[index++] = builder.build();
		}
		return new Namespace(name, finishedElements);
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
	public NamespaceBuilder createNamespace(String name, Set<Modifier> modifiers) throws ParsingException {
		
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
		namespace = new NamespaceBuilder(name, modifiers, this);
		elements.add(namespace);
		namespaces.add(namespace);
		return namespace;
	}
	
	public ClassBuilder createClass(String name) throws ParsingException {
		
		// Expanding classes is not allowed, so no other element with the same name may exist
		if (hasElement(name)) {
			throw new ParsingException("Multiple elements with name '" + name + "' in namespace " + this);
		}
		
		ClassBuilder classBuilder = new ClassBuilder(name, this);
		elements.add(classBuilder);
		classes.add(classBuilder);
		return classBuilder;
	}
	
	public VariableBuilder createVariable(String name, TypeBuilder type, ValueBuilder value) throws ParsingException {
		
		// I don't want multiple elements with the same name
		if (hasElement(name)) {
			throw new ParsingException("Multiple elements with name '" + name + "' in namespace " + this);
		}
		
		VariableBuilder varBuilder = new VariableBuilder(type, name, value);
		elements.add(varBuilder);
		variables.add(varBuilder);
		return varBuilder;
	}
	
	public boolean isGlobal() {
		return parent == null;
	}
	
	public boolean isConstant() {
		return modifiers.contains(Modifier.CONST);
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