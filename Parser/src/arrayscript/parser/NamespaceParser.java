package arrayscript.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.SmallParser.ModResult;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.NamespaceBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.HistorySourceFileReader;
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
		while (true) {

			SourceElement first = reader.next();

			// End of file is reached before the closing curly bracket
			if (first == null) {
				
				// The global namespace can not be closed explicitly and can be continued in the next file
				if (namespace.isGlobal()) {
					return;
				} else {
					throw new ParsingException("Unclosed namespace " + namespace);
				}
			}

			if (first.isOperator()) {

				// The namespace is being closed
				if (first.getOperator() == Operator.CLOSE_BLOCK) {
					break;
				}

				// No other operators are allowed at this position
				else {
					throw new ParsingException("Unexpected operator " + first.getOperator());
				}
			} else if (first.isWord() || first.isKeyword()) {
				
				// first is also a part of the modifiers, so use HistorySourceFileReader to pass it along
				ModResult foundModifiers = SmallParser.parseModifiers(new HistorySourceFileReader(reader, first));
				Set<Modifier> modifiers = foundModifiers.getModifiers();

				// Now that we have had all modifiers, the next source element(s) must be the type
				
				// parseModifiers consumes an extra source element, so use HistorySourceFileReader to pass
				// it along to the parseSomeType method
				SmallParser.SomeType nextType = SmallParser.parseSomeType(new HistorySourceFileReader(reader, foundModifiers.getNext()));

				// Distinguish between element types (class, namespace...) and variable types
				// (string,number...)
				if (nextType.isElementType()) {
					ElementType type = nextType.getElementType();

					if (type.needsName()) {

						// Observe that the name and opening curly bracket are read here
						SourceElement nameElement = nextType.getNext();

						if (nameElement == null) {
							throw new ParsingException(
									"Name of " + type + " was expected, but end of file was reached");
						}

						if (!nameElement.isWord()) {
							throw new ParsingException("Name of " + type + " was expected, but found " + nameElement);
						}

						String name = nameElement.getWord();
						SourceElement openCurly = reader.next();
						
						if (openCurly == null) {
							throw new ParsingException("Expected '{', but end of file was reached");
						}

						// All elements at this point must be defined with a { after the name
						if (!(openCurly.isOperator() && openCurly.getOperator() == Operator.OPEN_BLOCK)) {
							throw new ParsingException("Expected '{', but found " + openCurly);
						}

						// I hate switch
						if (type == ElementTypes.CLASS) {
							ClassParser.parseClass(reader, app, namespace.createClass(name, modifiers));
						} else if (type == ElementTypes.ENUM) {
							throw new ParsingException("Enums will be added in a later version");
						} else if (type == ElementTypes.GETTER) {
							throw new ParsingException("I am planning to add getters to namespaces later");
						} else if (type == ElementTypes.INIT) {
							app.registerInit(
									namespace.createInit(modifiers, name, ExecutableParser.parseInitial(reader)));
						} else if (type == ElementTypes.INTERFACE) {
							throw new ParsingException("Interfaces are not high on my priority list");
						} else if (type == ElementTypes.MAIN) {
							app.registerMain(
									namespace.createMain(modifiers, name, ExecutableParser.parseInitial(reader)));
							;
						} else if (type == ElementTypes.NAMESPACE) {
							parseNamespace(reader, app, namespace.createNamespace(name, modifiers));
						} else if (type == ElementTypes.SETTER) {
							throw new ParsingException("I am planning to add setters to namespaces later");
						} else {
							throw new Error("It looks like I forgot element type " + type.getName());
						}
					} else {

						// No name, so read the next curly bracket and start the parsing
						SourceElement openCurly = nextType.getNext();

						if (openCurly == null) {
							throw new ParsingException("Expected '{', but end of file was reached instead");
						}

						if (!(openCurly.isOperator() && openCurly.getOperator() == Operator.OPEN_BLOCK)) {
							throw new ParsingException("Expected '{', but found " + openCurly);
						}

						// Currently, constructor is the only element type that doesn't require a name
						if (type == ElementTypes.CONSTRUCTOR) {
							throw new ParsingException("You can't define a constructor in a namespace");
						} else {
							throw new Error("It looks like I forgot element type " + type.getName());
						}
					}
				} else {

					// Read the name of the variable/function
					SourceElement nameElement = nextType.getNext();

					// The name should be given and nothing else
					if (nameElement.isWord()) {
						String name = nameElement.getWord();

						// If a function is being declared, there will be brackets
						// If not, there must be an '='
						SourceElement maybeBracket = reader.next();
						
						if (maybeBracket == null) {
							throw new ParsingException("Expected '(' or '=', but end of file was reached");
						}
						
						if (!maybeBracket.isOperator()) {
							throw new ParsingException("Expected '(' or '=', but found " + maybeBracket);
						}
						if (maybeBracket.getOperator() == Operator.ASSIGNMENT) {

							// Everything until the ';' should be the unparsed initial value
							List<SourceElement> unparsedValueList = SmallParser.readUntilSemiColon(reader);
							
							// Observe that the semicolon was consumed by the readUntilSemiColon call
							namespace.createVariable(name, nextType.getVariableType(), modifiers, new ValueBuilder(unparsedValueList));
						} else if (maybeBracket.getOperator() == Operator.OPEN_BRACKET) {

							// Read the initial parameters and body
							ParamsBuilder parameters = ParamsParser.parse(reader);

							// The parameter parser won't read the opening '{', so do it here
							SourceElement openCurly = reader.next();

							if (openCurly == null) {
								throw new ParsingException(
										"Expected begin op function " + name + ", but end of file was reached");
							}

							if (!(openCurly.isOperator() && openCurly.getOperator() == Operator.OPEN_BLOCK)) {
								throw new ParsingException("Expected '{' to declare begin of function " + name
										+ ", but found " + openCurly);
							}

							// Gather the body
							List<SourceElement> body = ExecutableParser.parseInitial(reader);

							namespace.createFunction(name, nextType.getVariableType(), modifiers, parameters, body);
						} else {

							// Either ( to declare function or = to declare the value
							throw new ParsingException("Expected '(' or '=', but found " + maybeBracket);
						}
					} else {
						throw new ParsingException("A name for " + nextType + " was expected, but got " + nameElement);
					}
				}
			} else {

				// Strings are not allowed at this position
				throw new ParsingException("Unexpected string ('" + first.getStringContent() + "')");
			}
		}
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
		app.registerMain(namespace.createMain(modifiers, id, ExecutableParser.parseInitial(reader)));
	}

	@Override
	protected void defineNamespace(SourceFileReader reader, Set<Modifier> modifiers, String name)
			throws IOException, ParsingException {
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
		
		// Everything until the ';' should be the unparsed initial value
		List<SourceElement> unparsedValueList = SmallParser.readUntilSemiColon(reader);
		
		// Observe that the semicolon was consumed by the readUntilSemiColon call
		namespace.createVariable(name, type, modifiers, new ValueBuilder(unparsedValueList));
	}
}