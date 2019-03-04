package arrayscript.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.NamespaceBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class NamespaceParser extends AbstractNamespaceParser {

	/**
	 * Attempts to parse the content of a single namespace. This method must be
	 * called after the name of the namespace and the '{' have been read. The read
	 * name should be used to create the NamespaceBuilder instance that is required
	 * for this method. The namespace does not necessarily have to be empty, if it
	 * is not empty, the parsed content will be added to it. After the content is
	 * parsed, the closing '}' will be read.
	 * 
	 * @param reader    The reader that should be used to read the content
	 * @param app       The instance of the AppBuilder that is being used, it will
	 *                  be used to link certain elements if they are referred to.
	 * @param namespace The namespace builder where all parsed content will be added
	 *                  to
	 * @throws ParsingException If the provided source is not valid ArrayScript
	 * @throws IOException      if the provided reader throws an IOException
	 */
	public static void parseNamespace(SourceFileReader reader, AppBuilder app, NamespaceBuilder namespace)
			throws ParsingException, IOException {
		NamespaceParser parser = new NamespaceParser(namespace, app);
		parser.parse(reader, app);
	}
	
	private final NamespaceBuilder namespace;
	private final AppBuilder app;
	
	private NamespaceParser(NamespaceBuilder namespace, AppBuilder app) {
		this.namespace = namespace;
		this.app = app;
	}

	@Override
	protected void endOfFileBeforeClosed() throws ParsingException {
		if (namespace.isGlobal()) {
			return;
		} else {
			throw new ParsingException("Unclosed namespace " + namespace);
		}
	}

	@Override
	protected void defineClass(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);
		
		ClassParser.parseClass(reader, app, namespace.createClass(name, modifiers));
	}

	@Override
	protected void defineConstructor(SourceFileReader reader, Set<Modifier> modifiers)
			throws IOException, ParsingException {
		throw new ParsingException("You can't define constructors directly in namespaces.");
	}

	@Override
	protected void defineEnum(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("Enums will be added in a later version");
	}

	@Override
	protected void defineFunction(SourceFileReader reader, Set<Modifier> modifiers, TypeBuilder type, String name)
			throws IOException, ParsingException {
		
		// Read the initial parameters and body
		ParamsBuilder parameters = ParamsParser.parse(reader);

		// The parameter parser won't read the opening '{', so do it here
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);

		// Gather the body
		List<SourceElement> body = ExecutableParser.parseInitial(reader);

		namespace.createFunction(name, type, modifiers, parameters, body);
	}

	@Override
	protected void defineGetter(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("I am planning to add getters for namespaces in a later version");
	}

	@Override
	protected void defineInit(SourceFileReader reader, Set<Modifier> modifiers, String id)
			throws IOException, ParsingException {
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);
		app.registerInit(namespace.createInit(modifiers, id, ExecutableParser.parseInitial(reader)));
	}

	@Override
	protected void defineInterface(SourceFileReader reader, Set<Modifier> modifiers, String id)
			throws IOException, ParsingException {
		throw new ParsingException("Interfaces are not high on my priority list");
	}

	@Override
	protected void defineMain(SourceFileReader reader, Set<Modifier> modifiers, String id)
			throws IOException, ParsingException {
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);
		app.registerMain(namespace.createMain(modifiers, id, ExecutableParser.parseInitial(reader)));
	}

	@Override
	protected void defineNamespace(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		assumeOperator(reader.next(), Operator.OPEN_BLOCK);
		parseNamespace(reader, app, namespace.createNamespace(name, modifiers));
	}

	@Override
	protected void defineSetter(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
		throw new ParsingException("I am planning to add setters to namespaces later");
	}

	@Override
	protected void defineVariable(SourceFileReader reader, SourceElement nextElement, Set<Modifier> modifiers,
			TypeBuilder type, String name) throws IOException, ParsingException {
		if (nextElement == null) {
			throw new ParsingException("Expected '=', but end of file was reached");
		}
		
		// Variables in namespaces must have an initial value
		if (!nextElement.isOperator() || nextElement.getOperator() != Operator.ASSIGNMENT) {
			throw new ParsingException("Expected '=', but found " + nextElement);
		}
		
		// Everything until the ';' should be the unparsed initial value
		List<SourceElement> unparsedValueList = SmallParser.readUntilSemiColon(reader);
		
		// Observe that the semicolon was consumed by the readUntilSemiColon call
		namespace.createVariable(name, type, modifiers, new ValueBuilder(unparsedValueList));
	}

	@Override
	protected void addImport(String[] imported) throws ParsingException {
		namespace.addImport(imported);
	}
}