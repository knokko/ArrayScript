package arrayscript.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.lang.var.type.PrimitiveTypes;
import arrayscript.lang.var.type.Type;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.NamespaceBuilder;
import arrayscript.parser.builder.param.ParamsBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.builder.var.value.ValueBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

public class NamespaceParser {

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
				throw new ParsingException("Unclosed namespace " + namespace);
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
			} else if (first.isWord()) {

				// This is probably the type of the element that is about to be declared
				String typeName = first.getWord();

				Set<Modifier> modifiers = new HashSet<Modifier>(1);

				// All modifiers should be declared before the type (if there are any)
				while (Modifier.isModifier(typeName)) {

					// Don't allow duplicate modifiers
					if (!modifiers.add(Modifier.getByWord(typeName))) {
						throw new ParsingException("Duplicated modifier " + typeName);
					}

					SourceElement next = reader.next();

					// Either a modifier or typename must be given
					if (next.isWord()) {
						typeName = next.getWord();
					}

					// Nothing else
					else {
						throw new ParsingException("Unexpected " + next);
					}
				}

				// Now that we have had all modifiers, the typeName variable must be the actual
				// type name
				ElementType type = ElementTypes.getByName(typeName);

				// type names that do not have special meaning in source code will be treated as
				// normal names
				if (type == null || !type.shouldAppearInSource()) {

					// If the type is primitive, we can easily confirm it already
					// If not, we will check for the declaration in a later stage
					Type primitiveType = PrimitiveTypes.getByName(typeName);
					TypeBuilder typeBuilder;
					if (primitiveType == null) {
						typeBuilder = new TypeBuilder(typeName);
					} else {
						typeBuilder = new TypeBuilder(primitiveType);
					}

					// Read the name of the variable/function
					SourceElement nameElement = reader.next();

					// The name should be given and nothing else
					if (nameElement.isWord()) {
						String name = nameElement.getWord();
						if (name == null) {
							throw new ParsingException("The type " + typeName + " is not defined correctly");
						}

						// If a function is being declared, there will be brackets
						// If not, there must be an '='
						SourceElement maybeBracket = reader.next();
						if (!maybeBracket.isOperator()) {
							throw new ParsingException("Expected '(' or '=', but found " + maybeBracket);
						}
						if (maybeBracket.getOperator() == Operator.EQUALS) {

							// Should be followed by the equals sign and nothing else
							SourceElement equalsElement = reader.next();
							if (equalsElement.isOperator()) {

								// Everything until the ';' should be the unparsed initial value
								List<SourceElement> unparsedValueList = new ArrayList<SourceElement>();
								SourceElement partOfValue = reader.next();

								if (partOfValue == null) {
									throw new ParsingException("The end of file was reached before " + typeName + " "
											+ name + " started its initial value");
								}

								while (!(partOfValue.isOperator() && partOfValue.getOperator() == Operator.SEMICOLON)) {

									unparsedValueList.add(partOfValue);
									partOfValue = reader.next();

									// Throw a ParsingException instead of NullPointerException
									if (partOfValue == null) {
										throw new ParsingException("The variable " + name + " of type " + typeName
												+ " has an unfinished intial value.");
									}
								}

								namespace.createVariable(name, typeBuilder, new ValueBuilder(unparsedValueList));
							} else {
								throw new ParsingException("The equal sign was expected, but got " + equalsElement);
							}
						} else if (maybeBracket.getOperator() == Operator.OPEN_BRACKET){
							
							// Read the initial parameters and body
							ParamsBuilder parameters = ParamsParser.parse(reader);
							List<SourceElement> body = ExecutableParser.parseInitial(reader);

							namespace.createFunction(name, typeBuilder, parameters, body);
						}
					} else {
						throw new ParsingException("A name was expected, but got " + nameElement);
					}
				} else {
					if (type.needsName()) {

						// Observe that the name and opening curly bracket are read here
						SourceElement nameElement = reader.next();
						
						if (nameElement == null) {
							throw new ParsingException("Name of " + type + " was expected, but end of file was reached");
						}
						
						if (!nameElement.isWord()) {
							throw new ParsingException("Name of " + type + " was expected, but found " + nameElement);
						}
						
						
						String name = nameElement.getWord();
						SourceElement openCurly = reader.next();
						
						// All elements at this point must be defined with a { after the name
						if (!(openCurly.isOperator() && openCurly.getOperator() == Operator.OPEN_BLOCK)) {
							throw new ParsingException("Expected '{', but found " + openCurly);
						}

						// I hate switch
						if (type == ElementTypes.NAMESPACE) {
							parseNamespace(reader, app, namespace.createNamespace(name, modifiers));
						} else if (type == ElementTypes.CLASS) {
							// TODO parse class
						} else if (type == ElementTypes.INTERFACE) {
							throw new ParsingException("Interfaces are not high on my priority list");
						} else if (type == ElementTypes.ENUM) {
							// TODO parse enum
						} else if (type == ElementTypes.INIT) {
							// TODO parse code block and register init
						} else if (type == ElementTypes.MAIN) {
							// TODO parse code block and register main
						} else {
							throw new Error(
									"Did I forget a named type that should be in source?" + type.getClass().getName());
						}
					} else {

						// No name, so read the next curly bracket and start the parsing
						SourceElement openCurly = reader.next();
						
						if (openCurly == null) {
							throw new ParsingException("Expected '{', but end of file was reached instead");
						}
						
						if (!(openCurly.isOperator() && openCurly.getOperator() == Operator.OPEN_BLOCK)) {
							throw new ParsingException("Expected '{', but found " + openCurly);
						}

						// Ehm... well... I initially designed main and init not to have names, but I
						// changed my mind and now all current element types have names

						throw new ParsingException(
								"All current types need names, but this type is " + type.getClass().getName());
					}
				}
			} else {

				// Strings are not allowed at this position
				throw new ParsingException("Unexpected string ('" + first.getStringContent() + "')");
			}
		}
	}
}