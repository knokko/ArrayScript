package arrayscript.parser.builder;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.Namespace;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

public class NamespaceBuilder implements ElementBuilder {
	
	private final NamespaceBuilder parent;
	private final String name;
	
	private final Set<Modifier> modifiers;
	
	private final Collection<ElementBuilder> elements;
	
	private final Collection<NamespaceBuilder> namespaces;
	private final Collection<ClassBuilder> classes;
	private final Collection<VariableBuilder> variables;
	private final Collection<FunctionBuilder> functions;
	private final Collection<InitBuilder> inits;
	private final Collection<MainBuilder> mains;
	
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
		this.functions = new ArrayList<FunctionBuilder>();
		this.inits = new ArrayList<InitBuilder>(1);
		this.mains = new ArrayList<MainBuilder>(0);
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
	
	/**
	 * Adds a class with the given name to this namespace and returns the class builder that will be used
	 * to create the class. If this namespace already has an element with the given name, a ParsingException
	 * will be thrown.
	 * @param name The name of the class that should be created
	 * @return The class builder for the new class
	 * @throws ParsingException If this namespace already has an element with the given name
	 */
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
	
	/**
	 * Adds a variable with the given name, type and initial value to this namespace. The variable builder
	 * for the new variable will be returned if the variable was added successfully. If this namespace has
	 * another element with the given name, a ParsingException will be thrown instead. The name has to be
	 * known exactly already, but the type and value do not have to be fully resolved. (So its ok if only
	 * the name of the type is known.)
	 * @param name The name of the variable to add
	 * @param type The (unfinished) type of the variable
	 * @param value The (unfinished) initial value of the variable
	 * @return The variable builder for the new variable
	 * @throws ParsingException If this namespace already has an element with the given name
	 */
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
	
	/**
	 * Adds a function with the given name, return type, parameters and body to this namespace. Only the name
	 * has to be known exactly at this point. The return type, parameters and body do not need to be fully
	 * resolved by the parser. If the function was added successfully, the function builder for the new
	 * function will be returned. If not, a ParsingException will be thrown.
	 * @param name The name of the function to add
	 * @param returnType The (unfinished) return type of the function to add
	 * @param parameters The (unfinished) parameters of the function to add
	 * @param body The (unfinished) body of the function to add
	 * @return The function builder for the new function
	 * @throws ParsingException If the function could not be added
	 */
	public FunctionBuilder createFunction(String name, TypeBuilder returnType, ParamsBuilder parameters, List<SourceElement> body) throws ParsingException {
		
		// It is allowed to have multiple functions with the same name as long as they have different params
		for (ElementBuilder element : elements) {
			if (!(element instanceof FunctionBuilder) && element.getName().equals(name)) {
				throw new ParsingException("Multiple elemetns with name '" + name + "' in namespace " + this);
			}
		}
		
		FunctionBuilder functionBuilder = new FunctionBuilder(name, returnType, parameters, new ExecutableBuilder(body));
		elements.add(functionBuilder);
		functions.add(functionBuilder);
		return functionBuilder;
	}
	
	/**
	 * Adds an init with the given modifiers, name and body to this namespace. The modifiers must be empty or
	 * null. The name must be known exactly and the body doesn't have to be parsed at this point. If the init
	 * was added successfully, the init builder for the new init will be returned. If not, a ParsingException
	 * will be thrown. Note that the returned init builder MUST be registered to the app builder as well!
	 * @param modifiers The modifiers of the init to add, must be null or empty
	 * @param name The name of the init to add
	 * @param body The (unfinished) body of the init to add
	 * @return The init builder for the new init. Register this to the application builder!
	 * @throws ParsingException If the init can't be added to this namespace.
	 */
	public InitBuilder createInit(Collection<Modifier> modifiers, String name, List<SourceElement> body) throws ParsingException {
		
		// The application builder will throw a parsing exception if multiple inits share the same name
		// So no need to check for duplicated inits in this namespace
		
		InitBuilder initBuilder = new InitBuilder(this, modifiers, name, new ExecutableBuilder(body));
		elements.add(initBuilder);
		inits.add(initBuilder);
		
		return initBuilder;
	}
	
	/**
	 * Adds a main with the given modifiers, name and body to this namespace. The modifiers must be empty or
	 * null. The name must be known exactly and the body doesn't have to be parsed at this point. If the main
	 * was added successfully, the main builder for the new main will be returned. If not, a ParsingException
	 * will be thrown. Note that the returned main builder MUST be registered to the app builder as well!
	 * @param modifiers The modifiers of the main to add, must be null or empty
	 * @param name The name of the main to add
	 * @param body The (unfinished) body of the main to add
	 * @return The main builder for the new main. Register this to the application builder!
	 * @throws ParsingException If the main can't be added to this namespace.
	 */
	public MainBuilder createMain(Collection<Modifier> modifiers, String name, List<SourceElement> body) throws ParsingException {
		
		// The application builder will throw a parsing exception if multiple mains share the same name
		// So no need to check for duplicated mains in this namespace
		
		MainBuilder mainBuilder = new MainBuilder(this, modifiers, name, new ExecutableBuilder(body));
		elements.add(mainBuilder);
		mains.add(mainBuilder);
		
		return mainBuilder;
	}
	
	public boolean isGlobal() {
		return parent == null;
	}
	
	public boolean isClosed() {
		return !modifiers.contains(Modifier.OPEN);
	}
	
	public boolean isOpen() {
		return modifiers.contains(Modifier.OPEN);
	}
	
	public boolean hasElement(String name) {
		for (ElementBuilder element : elements) {
			
			// Inits are special elements because they can not be referred to
			if (!(element instanceof InitBuilder) && element.getName().equals(name)) {
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
	
	public void printTest1(PrintStream out, int indentLevel) {
		out.println();
		for (NamespaceBuilder namespace : namespaces) {
			printTest1(out, indentLevel, "namespace " + collectionToString(namespace.modifiers) + " " + namespace.name + " {");
			namespace.printTest1(out, indentLevel + 1);
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (ClassBuilder cb : classes) {
			printTest1(out, indentLevel, "class " + cb.getName() + "{");
			// TODO expand class builders
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (VariableBuilder variable : variables) {
			printTest1(out, indentLevel, variable.getType().getReadableTypeName() + " " + variable.getName() + " = ... ;");
			out.println();
		}
		for (FunctionBuilder function : functions) {
			printTest1(out, indentLevel, function.getReturnType().getReadableTypeName() + " " + function.getName() + "( ... ) {");
			// TODO expand function body
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (InitBuilder init : inits) {
			printTest1(out, indentLevel, "init " + init.getName() + ";");
			out.println();
		}
		for (MainBuilder main : mains) {
			printTest1(out, indentLevel, "main " + main.getName() + ";");
		}
		out.println();
	}
	
	private void printTest1(PrintStream out, int indentLevel, String message) {
		for (int counter = 0; counter < indentLevel; counter++) {
			out.print("    ");
		}
		out.println(message);
	}
	
	private String collectionToString(Collection<? extends Object> collection) {
		if (collection.isEmpty()) {
			return "";
		}
		String[] strings = new String[collection.size()];
		int size = 0;
		int index = 0;
		for (Object next : collection) {
			strings[index] = next.toString();
			
			// for the whitespace between the elements
			size += strings[index].length() + 1;
			index++;
		}
		
		// The last element doesn't need a whitespace at the end
		size--;
		StringBuilder builder = new StringBuilder(size);
		for (index = 0; index < strings.length - 1; index++) {
			builder.append(strings[index]);
			builder.append(' ');
		}
		builder.append(strings[strings.length - 1]);
		return builder.toString();
	}
}