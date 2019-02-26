package arrayscript.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.ClassBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.executable.ExecutableBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class ClassParser extends AbstractNamespaceParser {
	
	@SuppressWarnings("unused")
	private final AppBuilder app;
	private final ClassBuilder classBuilder;
	
	private ClassParser(AppBuilder app, ClassBuilder classBuilder) {
		this.app = app;
		this.classBuilder = classBuilder;
	}
	
	/**
	 * Attempts to parse the content of a single class and adds the parsed content to the provided class
	 * builder. This method must be called after the name of the class and the opening '{' have been read. The
	 * read name should be used to create the class builder that should be provided as parameter to this
	 * method. After the content is read and parsed, the closing '}' will be read by this method.
	 * @param reader The reader to read the source code for this class from
	 * @param app The instance of the app builder
	 * @param classBuilder The class builder that this method should parse content for
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException or if the class is not
	 * defined correctly in the source code
	 */
	public static void parseClass(SourceFileReader reader, AppBuilder app, ClassBuilder classBuilder) throws IOException, ParsingException {
		ClassParser instance = new ClassParser(app, classBuilder);
		instance.parse(reader, app);
	}

	@Override
	protected void endOfFileBeforeClosed() throws ParsingException {
		throw new ParsingException("End of file was reached before this class was closed");
	}

	@Override
	protected void defineClass(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("I think I will allow inner classes later");
	}

	@Override
	protected void defineConstructor(SourceFileReader reader, Set<Modifier> modifiers)
			throws IOException, ParsingException {
		SourceElement openBracket = reader.next();
		if (openBracket == null) {
			throw new ParsingException("Expected '(', but end of file was reached");
		}
		if (!openBracket.isOperator() || openBracket.getOperator() != Operator.OPEN_BRACKET) {
			throw new ParsingException("Expected '(', but found " + openBracket);
		}
		
		ParamsBuilder params = ParamsParser.parse(reader);
		
		SourceElement openCurly = reader.next();
		if (openCurly == null) {
			throw new ParsingException("Expected '{', but end of file was reached");
		}
		if (!openCurly.isOperator() || openCurly.getOperator() != Operator.OPEN_BLOCK) {
			throw new ParsingException("Expected '{', but found " + openCurly);
		}
		
		List<SourceElement> head = SmallParser.readBlock(reader);
		
		openCurly = reader.next();
		if (openCurly == null) {
			throw new ParsingException("Expected the '{' to start the constructor body, but end of file was reached");
		}
		if (!openCurly.isOperator() || openCurly.getOperator() != Operator.OPEN_BLOCK) {
			throw new ParsingException("Expected the '{' to start the constructor body, but found " + openCurly);
		}
		
		ExecutableBuilder body = new ExecutableBuilder(ExecutableParser.parseInitial(reader));
		
		classBuilder.addConstructor(modifiers, params, head, body);
	}

	@Override
	protected void defineEnum(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("Enums are not very high on my priority list");
	}

	@Override
	protected void defineFunction(SourceFileReader reader, Set<Modifier> modifiers, TypeBuilder type, String name)
			throws IOException, ParsingException {
		
		// First read the parameters and function body (but don't try to understand it)
		ParamsBuilder parameters = ParamsParser.parse(reader);
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);
		ExecutableBuilder body = new ExecutableBuilder(ExecutableParser.parseInitial(reader));
		
		// If static add function, otherwise add method
		if (modifiers.remove(Modifier.STATIC)) {
			
			classBuilder.addFunction(name, modifiers, type, parameters, body);
		} else {
			classBuilder.addMethod(name, modifiers, type, parameters, body);
		}
	}

	@Override
	protected void defineGetter(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		SourceElement colonOrCurly = reader.next();
		
		if (colonOrCurly == null) {
			throw new ParsingException("Expected a ';' or '{' after getter " + name + ", but end of file was reached");
		}
		
		if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.SEMICOLON) {
			
			// A default getter is being defined
			classBuilder.addDefaultGetter(name, modifiers);
		} else if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.OPEN_BLOCK) {
			
			// The programmer creates a getter with a custom body
			classBuilder.addCustomGetter(name, modifiers, new ExecutableBuilder(ExecutableParser.parseInitial(reader)));
		} else {
			throw new ParsingException("Expected a ';' or '{' after getter " + name + ", but found " + colonOrCurly);
		}
	}

	@Override
	protected void defineInit(SourceFileReader reader, Set<Modifier> modifiers, String id)
			throws IOException, ParsingException {
		throw new ParsingException("I think I will allow inits in classes later");
	}

	@Override
	protected void defineInterface(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("Interfaces are not high on my priority list");
	}

	@Override
	protected void defineMain(SourceFileReader reader, Set<Modifier> modifiers, String id)
			throws IOException, ParsingException {
		throw new ParsingException("I don't think I will allow mains in classes");
	}

	@Override
	protected void defineNamespace(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("I think I will allow namespaces in classes later");
	}

	@Override
	protected void defineSetter(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		SourceElement colonOrCurly = reader.next();
		
		if (colonOrCurly == null) {
			throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but end of file was reached");
		}
		
		if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.SEMICOLON) {
			
			// A default getter is being defined
			classBuilder.addDefaultSetter(name, modifiers);
		} else if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.OPEN_BLOCK) {
			
			// The programmer creates a getter with a custom body
			classBuilder.addCustomSetter(name, modifiers, new ExecutableBuilder(ExecutableParser.parseInitial(reader)));
		} else {
			throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but found " + colonOrCurly);
		}
	}

	@Override
	protected void defineVariable(SourceFileReader reader, SourceElement nextElement, Set<Modifier> modifiers,
			TypeBuilder type, String name) throws IOException, ParsingException {
		ValueBuilder defaultValue;
		
		// The value is directly being defined
		if (nextElement.isOperator() && nextElement.getOperator() == Operator.ASSIGNMENT) {
			defaultValue = new ValueBuilder(SmallParser.readUntilSemiColon(reader));
		}
		
		// No default value is given, only allow this on properties, not on variables
		else if (nextElement.isOperator() && nextElement.getOperator() == Operator.SEMICOLON) {
			defaultValue = null;
		}
		
		// Just don't allow anything else
		else {
			throw new ParsingException("Expected ';' or '=', but found " + nextElement);
		}
		if (modifiers.remove(Modifier.STATIC)) {
			
			// A variable is being declared
			if (defaultValue == null) {
				throw new ParsingException("Static class properties need an initial value");
			}
			classBuilder.addVariable(name, modifiers, type, defaultValue);
		} else {
			
			// A property is being declared
			classBuilder.addProperty(name, type, modifiers, defaultValue);
		}
	}
}