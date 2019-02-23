package arrayscript.parser;

import java.io.IOException;
import java.util.Set;

import arrayscript.lang.Modifier;
import arrayscript.lang.Operator;
import arrayscript.lang.element.ElementType;
import arrayscript.lang.element.ElementTypes;
import arrayscript.parser.SmallParser.ModResult;
import arrayscript.parser.builder.AppBuilder;
import arrayscript.parser.builder.var.type.TypeBuilder;
import arrayscript.parser.source.SourceElement;
import arrayscript.parser.source.reading.HistorySourceFileReader;
import arrayscript.parser.source.reading.SourceFileReader;
import arrayscript.parser.util.ParsingException;

abstract class AbstractNamespaceParser {
	
	/**
	 * This will be called when the end of the source file is reached before the abstract namespace is closed
	 * with a closing curly bracket. Subclasses should override this and throw a ParsingException unless this
	 * is for the global namespace which should not be closed.
	 */
	protected abstract void endOfFileBeforeClosed() throws ParsingException;
	
	/**
	 * This method will be called when a class is about to be defined in this abstract namespace. It will be
	 * called after the reader has read the name of the class, but before the opening curly bracket. It is
	 * up to the subclass that overrides this method to determine whether or not a class can have the given
	 * modifiers and if the class body is correct. A ParsingException should be thrown if this is not the
	 * case.
	 * @param reader The reader that is reading the current source file
	 * @param modifiers The modifiers that the class should get
	 * @param name The name that the class should get
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException or if the class modifiers
	 * are invalid or if the class body is not defined correctly.
	 */
	protected abstract void defineClass(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	/**
	 * This method will be called when a constructor is about to be defined in this abstract namespace. It
	 * will be called after the reader has read the 'constructor' keyword, but before the opening bracket is
	 * read. It is up to the subclass that overrides this method to determine whether a constructor with the
	 * given modifiers can be defined. If that is not allowed, a ParsingException should be thrown. A
	 * ParsingException should also be thrown if the parameters or body of the constructor are not defined
	 * correctly.
	 * @param reader The reader that is reading the constructor
	 * @param modifiers The modifiers that the constructor should have
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException, constructors with the given
	 * modifiers are not allowed or if the constructor is not defined correctly.
	 */
	protected abstract void defineConstructor(SourceFileReader reader, Set<Modifier> modifiers) throws IOException, ParsingException;
	
	/**
	 * This method will be called when an enum is about to be defined. It will be called after the name of
	 * the enum has been read, but before the opening curly bracket is read. The subclass that overrides this
	 * method should determine whether enums are allowed to have the given modifiers and if the body is read
	 * correctly. If that is not the case, a ParsingException should be thrown.
	 * @param reader The reader that is reading the enum
	 * @param modifiers The modifiers the enum should get
	 * @param name The name the enum should get
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException, enums (with the given
	 * modifiers) are not allowed or if the enum is not defined correctly.
	 */
	protected abstract void defineEnum(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	/**
	 * This method will be called when a function is about to be defined in this abstract namespace. When
	 * this method is called, the function name and the opening bracket will have been read already. The
	 * subclass that overrides this method should read the parameters and body and determine whether or not
	 * it is valid. If not, a ParsingException should be thrown.
	 * @param reader The reader that is reading this source file
	 * @param modifiers The modifiers that the function should have
	 * @param type The type that the function should get
	 * @param name The name the function should get
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException or if the function is
	 * not defined correctly or can't be added.
	 */
	protected abstract void defineFunction(SourceFileReader reader, Set<Modifier> modifiers, TypeBuilder type, String name) throws IOException, ParsingException;
	
	/**
	 * This method will be called when a getter is about to be defined in this abstract namespace. The name
	 * of the getter will be read already, but the opening curly bracket or semicolon will not be read
	 * already. Checking whether or not a getter with the given name and modifiers can be defined is up to
	 * the subclass that overrides this method. If the getter is not correct, a ParsingException should be
	 * thrown.
	 * @param reader The reader that is reading this source file
	 * @param modifiers The modifiers that the getter should get
	 * @param name The name of the getter (variable)
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException, a getter with the given
	 * name and modifiers can't be created or if the getter is not defined correctly
	 */
	protected abstract void defineGetter(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	/**
	 * This method will be called when an init is about to be defined in this abstract namespace. The id/name
	 * of the init will be read already, but the opening curly bracket will not be read already. The subclass
	 * that is overriding this method should parse the init and register it to the application. If the init
	 * can't be parsed or registered, a ParsingException should be thrown.
	 * @param reader The reader that is reading this source file
	 * @param modifiers The modifiers that the init should have
	 * @param id The id that the init should have
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException, the init is not defined
	 * correctly or if the init can't be registered to the application builder
	 */
	protected abstract void defineInit(SourceFileReader reader, Set<Modifier> modifiers, String id) throws IOException, ParsingException;
	
	/**
	 * This method will be called when an interface is about to be defined. The name of the interface will
	 * already be read, but the opening curly bracket will not be read yet. The subclass that overrides this
	 * method should try to parse the interface and add it. If the interface is not defined correctly or
	 * can't be added, a ParsingException should be thrown.
	 * @param reader The reader that is reading this source file
	 * @param modifiers The modifiers that the interface should get
	 * @param name The name that the interface should get
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException If the provided reader throws a ParsingException, the interface is not
	 * defined correctly or the interface can't be added.
	 */
	protected abstract void defineInterface(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	/**
	 * This method will be called when a main is about to be defined. The id of the main will already be
	 * read, but the opening curly bracket will not be read already. The subclass that overrides this method
	 * should parse the main body and register the main to the app builder. If the main is not defined
	 * correctly or can't be registered, a ParsingException should be thrown.
	 * @param reader The reader that is reading this source file
	 * @param modifiers The modifiers that the main should get
	 * @param id The id that the main should get
	 * @throws IOException If the provided reader throws an IOException
	 * @throws ParsingException The the provided reader throws a ParsingException, the main is not defined
	 * correctly or the main can't be registered
	 */
	protected abstract void defineMain(SourceFileReader reader, Set<Modifier> modifiers, String id) throws IOException, ParsingException;
	
	/**
	 * 
	 * @param reader
	 * @param modifiers
	 * @param name
	 * @throws IOException
	 * @throws ParsingException
	 */
	protected abstract void defineNamespace(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	protected abstract void defineSetter(SourceFileReader reader, Set<Modifier> modifiers, String name) throws IOException, ParsingException;
	
	protected abstract void defineVariable(SourceFileReader reader, SourceElement nextElement, Set<Modifier> modifiers, TypeBuilder type, String name) throws IOException, ParsingException;
	
	protected void assumeOperator(SourceElement nextElement, Operator operator) throws ParsingException {
		
		if (nextElement == null) {
			throw new ParsingException("Expected '" + operator.getTokens() + "', but end of file was reached");
		}
		
		if (!nextElement.isOperator() || nextElement.getOperator() != operator) {
			throw new ParsingException("Expected '" + operator.getTokens() + "', but found " + nextElement);
		}
	}
	
	public void parse(SourceFileReader reader, AppBuilder app) throws IOException, ParsingException {
		
		// Breaking when we encounter the end is easier than a proper loop termination condition
		while (true) {

			SourceElement first = reader.next();

			// End of file is reached before the closing curly bracket
			if (first == null) {
				
				endOfFileBeforeClosed();
				
				/*
				// The global namespace can not be closed explicitly and can be continued in the next file
				if (namespace.isGlobal()) {
					return;
				} else {
					throw new ParsingException("Unclosed namespace " + namespace);
				}*/
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

						// I hate switch
						if (type == ElementTypes.CLASS) {
							defineClass(reader, modifiers, name);
						} else if (type == ElementTypes.ENUM) {
							defineEnum(reader, modifiers, name);
						} else if (type == ElementTypes.GETTER) {
							defineGetter(reader, modifiers, name);
						} else if (type == ElementTypes.INIT) {
							defineInit(reader, modifiers, name);
						} else if (type == ElementTypes.INTERFACE) {
							defineInterface(reader, modifiers, name);
						} else if (type == ElementTypes.MAIN) {
							defineMain(reader, modifiers, name);
						} else if (type == ElementTypes.NAMESPACE) {
							defineNamespace(reader, modifiers, name);
						} else if (type == ElementTypes.SETTER) {
							defineSetter(reader, modifiers, name);
						} else {
							throw new Error("It looks like I forgot element type " + type.getName());
						}
					} else {

						// Currently, constructor is the only element type that doesn't require a name
						if (type == ElementTypes.CONSTRUCTOR) {
							defineConstructor(reader, modifiers);
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
						
						if (maybeBracket.isOperator() && maybeBracket.getOperator() == Operator.OPEN_BRACKET) {
							defineFunction(reader, modifiers, nextType.getReturnType(), name);
						} else if (nextType.isVariableType()){
							defineVariable(reader, maybeBracket, modifiers, nextType.getVariableType(), name);
						} else {
							throw new ParsingException("void can only be used as function return type, but not as variable type");
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
}