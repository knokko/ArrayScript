package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
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
		
		PropertyBuilder prop = new PropertyBuilder(name, type, modifiers, defaultValue);
		elements.add(prop);
		properties.add(prop);
	}
	
	public void addDefaultGetter(String name, Set<Modifier> modifiers) throws ParsingException {
		addGetter(new GetterBuilder(name, modifiers));
	}
	
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
}