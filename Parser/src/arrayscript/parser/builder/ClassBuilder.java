package arrayscript.parser.builder;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.util.ParsingException;

public class ClassBuilder implements ElementBuilder {
	
	private final String name;
	private final NamespaceBuilder namespace;
	private final Set<Modifier> modifiers;
	
	private final Collection<ElementBuilder> elements;
	private final Collection<FunctionBuilder> functions;
	private final Collection<VariableBuilder> variables;
	private final Collection<MethodBuilder> methods;
	private final Collection<PropertyBuilder> properties;
	private final Collection<GetterBuilder> getters;
	private final Collection<SetterBuilder> setters;
	private final Collection<ConstructorBuilder> constructors;
	
	public ClassBuilder(String name, NamespaceBuilder namespace, Set<Modifier> modifiers) throws ParsingException {
		this.name = name;
		this.namespace = namespace;
		
		for (Modifier modifier : modifiers) {
			if (!ElementTypes.CLASS.canHave(modifier)) {
				throw new ParsingException("Classes can't have the " + modifier + " modifier");
			}
		}
		this.modifiers = modifiers;
		
		this.elements = new ArrayList<ElementBuilder>(20);
		this.functions = new ArrayList<FunctionBuilder>(2);
		this.variables = new ArrayList<VariableBuilder>(2);
		this.methods = new ArrayList<MethodBuilder>(20);
		this.properties = new ArrayList<PropertyBuilder>(10);
		this.getters = new ArrayList<GetterBuilder>(5);
		this.setters = new ArrayList<SetterBuilder>(2);
		this.constructors = new ArrayList<ConstructorBuilder>(1);
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
	
	public Set<Modifier> getModifiers(){
		return modifiers;
	}
	
	/**
	 * Attempts to add a function to this class. If the function is added successfully, this method will
	 * return silently. If not, a ParsingException will be thrown.
	 * @param name The name of the function
	 * @param modifiers The modifiers of the function. The static modifier should be removed already
	 * @param returnType The (possibly unfinished) return type of the function, or null if the function 
	 * doesn't have a return type
	 * @param parameters The (possibly unfinished) parameters of the function, possibly empty
	 * @param body The (possibly unfinished) function body
	 * @throws ParsingException If the function can't be added to this class
	 */
	public void addFunction(String name, Set<Modifier> modifiers, TypeBuilder returnType, ParamsBuilder parameters, ExecutableBuilder body) throws ParsingException {
		
		// Functions with the same name and conflicting parameters are not allowed
		for (FunctionBuilder function : functions) {
			if (function.getName().equals(name) && function.getParameters().conflicts(parameters)) {
				throw new ParsingException("Duplicated function " + name + " in class " + this.name);
			}
		}
		
		FunctionBuilder function = new FunctionBuilder(name, modifiers, returnType, parameters, body);
		elements.add(function);
		functions.add(function);
	}
	
	/**
	 * Attempts to create a method will the give name, return type, parameters and body to this class. If the
	 * method is added successfully, this will return silently. If not, a ParsingException will be thrown.
	 * @param name The name of the method that is about to be added
	 * @param modifiers The modifiers of the method
	 * @param returnType The (possibly unfinished) return type of the method that is about to be added, 
	 * possibly null
	 * @param parameters The (possibly unfinished) parameters of this method
	 * @param body The (unfinished) method body
	 * @throws ParsingException If the method can't be added
	 */
	public void addMethod(String name, Set<Modifier> modifiers, TypeBuilder returnType, ParamsBuilder parameters, ExecutableBuilder body) throws ParsingException {
		
		// Methods with same name and conflicting parameters are not allowed
		for (MethodBuilder method : methods) {
			if (method.getName().equals(name) && method.getParameters().conflicts(parameters)) {
				throw new ParsingException("Duplicated method " + name + " in class " + this.name);
			}
		}
		
		// Don't allow ambiguity between method calls and getter class
		for (GetterBuilder getter : getters) {
			if (getter.getMethodName().equals(name)) {
				throw new ParsingException("The method " + name + " will have the same effective name as a getter in class " + this.name);
			}
		}
		
		MethodBuilder method = new MethodBuilder(name, returnType, parameters, body, modifiers);
		elements.add(method);
		methods.add(method);
	}
	
	/**
	 * Attempts to add a variable with the given name, modifiers, type and default value to this class
	 * builder. The given modifiers should NOT contain the static modifier, it should only be used to
	 * distinguish variables from properties, which already happened because this method is being called and
	 * not the addProperty method. The type and default value do not have to be fully resolved already, 
	 * that will be done in a later parsing stage.
	 * @param name The name of the variable to add
	 * @param modifiers The modifiers of the variable to add, excluding the static modifier
	 * @param type The type of the variable to add, possibly unfinished/unresolved
	 * @param defaultValue The default value of the variable, possibly unfinished/unresolved
	 * @throws ParsingException If the variable can't be added to this class builder
	 */
	public void addVariable(String name, Set<Modifier> modifiers, TypeBuilder type, ValueBuilder defaultValue) throws ParsingException {
		for (VariableBuilder var : variables) {
			if (var.getName().equals(name)) {
				throw new ParsingException("Two variables in class " + this.name + " share the name " + name);
			}
		}
		
		VariableBuilder var = new VariableBuilder(modifiers, type, name, defaultValue);
		elements.add(var);
		variables.add(var);
	}
	
	/**
	 * Attempts to add a property with the given name, (unfinished) type, modifiers and (unfinished) default
	 * value to this class builder. The type and defautl value do not have to be resolved/finished
	 * completely, this will be finished at a later parsing stage. The default value is allowed to be null,
	 * in that case, this property will not have a default value.
	 * @param name The name of the property to add
	 * @param type The (unfinished) type of the property to add
	 * @param modifiers The modifiers of the property to add
	 * @param defaultValue The optional (unfinished) default value of the property to add
	 * @throws ParsingException If the property can't be added to this class builder
	 */
	public void addProperty(String name, TypeBuilder type, Set<Modifier> modifiers, ValueBuilder defaultValue) throws ParsingException {
		for (PropertyBuilder property : properties) {
			if (property.getName().equals(name)) {
				throw new ParsingException("Two properties of class " + this.name + " have the same name");
			}
		}
		
		PropertyBuilder prop;
		if (defaultValue != null) {
			prop = new PropertyBuilder(name, type, modifiers, defaultValue);
		} else {
			prop = new PropertyBuilder(name, type, modifiers);
		}
		elements.add(prop);
		properties.add(prop);
	}
	
	/**
	 * Attempts to add a default getter with the given modifiers for the property with the given name. This 
	 * will NOT check if this class has a property with the given name, that must be done in a later parsing
	 * stage.
	 * @param name The name of the property the getter is for
	 * @param modifiers The modifiers the getter should get
	 * @throws ParsingException If the getter can't be added to this class builder
	 */
	public void addDefaultGetter(String name, Set<Modifier> modifiers) throws ParsingException {
		addGetter(new GetterBuilder(name, modifiers));
	}
	
	/**
	 * Attempts to add a custom getter with the given modifiers and body for the property with the give name.
	 * This will NOT check if this class has a property with the given name, that must be done in a later
	 * parsing stage.
	 * @param name The name of the property the getter is for
	 * @param modifiers The modifiers the getter should get
	 * @param body The custom body the getter should get
	 * @throws ParsingException If the getter can't be added to this class builder
	 */
	public void addCustomGetter(String name, Set<Modifier> modifiers, ExecutableBuilder body) throws ParsingException {
		addGetter(new GetterBuilder(name, modifiers, body));
	}
	
	private void addGetter(GetterBuilder getter) throws ParsingException {
		
		// Don't allow ambiguity between getters and methods that will have the same effective name
		for (MethodBuilder method : methods) {
			if (method.getName().equals(getter.getMethodName())) {
				throw new ParsingException("The method " + method.getName() + " conflicts with a getter");
			}
		}
		
		// Don't allow two getters with the same name
		for (GetterBuilder get : getters) {
			if (get.getPropertyName().equals(getter.getPropertyName())) {
				throw new ParsingException("Multiple getters for property " + getter.getPropertyName() + " in class " + this.name);
			}
		}
		
		elements.add(getter);
		getters.add(getter);
	}
	
	/**
	 * Attempts to add a default setter with the given modifiers for the given property (name). This will
	 * NOT check if a property with the given name exists, that must be done in a later parsing stage.
	 * @param propertyName The name of the property the setter will be for
	 * @param modifiers The modifiers of the setter
	 * @throws ParsingException If the setter can't be added to this class builder
	 */
	public void addDefaultSetter(String propertyName, Set<Modifier> modifiers) throws ParsingException {
		addSetter(new SetterBuilder(propertyName, modifiers));
	}
	
	/**
	 * Attempts to add a custom setter with the given modifiers and body for the given property (name). This
	 * will NOT check if a property with the given name exists, that must be done in a later parsing stage.
	 * @param propertyName The name of the property the setter will be for
	 * @param modifiers The modifiers of the setter
	 * @param paramName The name of the 'newValue' parameter that should be used within the setter body
	 * @param body The custom body of the setter
	 * @throws ParsingException If the setter can't be added to this class builder
	 */
	public void addCustomSetter(String propertyName, Set<Modifier> modifiers, String paramName, ExecutableBuilder body) throws ParsingException {
		addSetter(new SetterBuilder(propertyName, modifiers, paramName, body));
	}
	
	private void addSetter(SetterBuilder setter) throws ParsingException {
		
		// At most 1 setter per property
		for (SetterBuilder current : setters) {
			if (current.getName().equals(setter.getName())) {
				throw new ParsingException("Multiple setters for property " + setter.getPropertyName() + " in class " + name);
			}
		}
		
		// Don't allow ambiguity between method calls and setter calls
		for (MethodBuilder method : methods) {
			if (method.getName().equals(setter.getMethodName())) {
				throw new ParsingException("The method " + method.getName() + " is the same as the effective name of setter " + setter.getName());
			}
		}
		
		elements.add(setter);
		setters.add(setter);
	}
	
	/**
	 * Attempts to add a constructor with the given modifiers, parameters, head and body to this class.
	 * @param modifiers The modifiers the constructor should get
	 * @param parameters The parameters the constructor should get
	 * @param head The head the constructor should get
	 * @param body The body the constructor should get
	 * @throws ParsingException If a constructor with the given modifiers, parameters, head and body can't
	 * be added to this class
	 */
	public void addConstructor(Set<Modifier> modifiers, ParamsBuilder parameters, List<SourceElement> head, ExecutableBuilder body) throws ParsingException {
		
		// No duplicated constructors
		for (ConstructorBuilder constructor : constructors) {
			if (constructor.getParameteters().conflicts(parameters)) {
				throw new ParsingException("Duplicated constructor in class " + name);
			}
		}
		
		ConstructorBuilder constructor = new ConstructorBuilder(modifiers, parameters, head, body);
		elements.add(constructor);
		constructors.add(constructor);
	}
	
	public void printTest1(PrintStream out, int indentLevel) {
		out.println();
		/*
		for (NamespaceBuilder namespace : namespaces) {
			printTest1(out, indentLevel, "namespace " + collectionToString(namespace.modifiers) + " " + namespace.name + " {");
			namespace.printTest1(out, indentLevel + 1);
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (ClassBuilder cb : classes) {
			printTest1(out, indentLevel, collectionToString(cb.getModifiers()) + " class " + cb.getName() + "{");
			cb.printTest1(out, indentLevel + 1);
			printTest1(out, indentLevel, "}");
			out.println();
		}*/
		for (VariableBuilder variable : variables) {
			printTest1(out, indentLevel, collectionToString(variable.getModifiers()) + " " + variable.getType().getReadableTypeName() + " " + variable.getName() + " = ... ;");
			out.println();
		}
		for (FunctionBuilder function : functions) {
			printTest1(out, indentLevel, collectionToString(function.getModifiers()) + " " + function.getReturnTypeName() + " " + function.getName() + "(" + function.getParameters() + ") {");
			printTest1(out, indentLevel + 1, "function body...");
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (ConstructorBuilder constructor : constructors) {
			printTest1(out, indentLevel, collectionToString(constructor.getModifiers()) + " constructor(" + constructor.getParameteters() + ") {...} {...}");
		}
		for (MethodBuilder method : methods) {
			printTest1(out, indentLevel, collectionToString(method.getModifiers()) + " " + method.getReturnTypeName() + " " + method.getName() + "(" + method.getParameters() + "){");
			printTest1(out, indentLevel + 1, "method body...");
			printTest1(out, indentLevel, "}");
			out.println();
		}
		for (PropertyBuilder prop : properties) {
			if (prop.hasDefaultValue()) {
				printTest1(out, indentLevel, collectionToString(prop.getModifiers()) + " " + prop.getType().getReadableTypeName() + " " + prop.getName() + " = ... ;");
			} else {
				printTest1(out, indentLevel, collectionToString(prop.getModifiers()) + " " + prop.getType().getReadableTypeName() + " " + prop.getName() + ";");
			}
		}
		for (GetterBuilder getter : getters) {
			if (getter.hasCustomBody()) {
				printTest1(out, indentLevel, collectionToString(getter.getModifiers()) + " getter " + getter.getName() + "{ ... }");
			} else {
				printTest1(out, indentLevel, collectionToString(getter.getModifiers()) + " getter " + getter.getName() + ";");
			}
		}
		for (SetterBuilder setter : setters) {
			if (setter.isDefault()) {
				printTest1(out, indentLevel, collectionToString(setter.getModifiers()) + " setter " + setter.getName() + ";");
			} else {
				printTest1(out, indentLevel, collectionToString(setter.getModifiers()) + " setter " + setter.getName() + " (newValue) { ... }");
			}
		}
		/*
		for (InitBuilder init : inits) {
			printTest1(out, indentLevel, "init " + init.getName() + ";");
			out.println();
		}
		for (MainBuilder main : mains) {
			printTest1(out, indentLevel, "main " + main.getName() + ";");
		}*/
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