package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.element.Element;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
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
		
		MethodBuilder method = new MethodBuilder(name, returnType, parameters, body, modifiers);
		elements.add(method);
		methods.add(method);
	}
	
	// TODO Create method to add variables or properties
}