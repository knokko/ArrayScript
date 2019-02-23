package arrayscript.parser;

import java.io.IOException;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.ClassBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
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
		// TODO Parse constructor
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
		if (modifiers.contains(Modifier.STATIC)) {
			
			modifiers.remove(Modifier.STATIC);
			classBuilder.addFunction(name, modifiers, type, parameters, body);
		} else {
			classBuilder.addMethod(name, modifiers, type, parameters, body);
		}
	}

	@Override
	protected void defineGetter(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		// TODO parse getter
		SourceElement colonOrCurly = reader.next();
		
		if (colonOrCurly == null) {
			throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but end of file was reached");
		}
		
		if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.SEMICOLON) {
			
			// A default getter is being defined
		} else if (colonOrCurly.isOperator() && colonOrCurly.getOperator() == Operator.OPEN_BLOCK) {
			
			// The programmer creates a getter with a custom body
		} else {
			throw new ParsingException("Expected a ';' or '{' after setter " + name + ", but found " + colonOrCurly);
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
		// TODO Parse setter
	}

	@Override
	protected void defineVariable(SourceFileReader reader, SourceElement nextElement, Set<Modifier> modifiers,
			TypeBuilder type, String name) throws IOException, ParsingException {
		// TODO Add property or variable
	}
}