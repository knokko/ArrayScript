package arrayscript.parser.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import arrayscript.lang.Application;
import arrayscript.parser.util.ParsingException;

public class AppBuilder {
	
	private final NamespaceBuilder globalNamespace;
	
	private final Collection<InitBuilder> inits;
	private final Collection<MainBuilder> mains;
	
	public AppBuilder() {
		try {
			globalNamespace = new NamespaceBuilder(null, Collections.emptySet(), null);
		} catch (ParsingException e) {
			throw new Error("Should not be possible", e);
		}
		inits = new ArrayList<InitBuilder>(200);
		mains = new ArrayList<MainBuilder>(1);
	}
	
	public Application build() {
		return new Application(globalNamespace.build());
	}
	
	public NamespaceBuilder getGlobalNamespace() {
		return globalNamespace;
	}
	
	/**
	 * Registers an init to this application. All inits must be registered here to make sure they will be
	 * executed exactly once before the application starts. Multiple inits with the same name are not allowed.
	 * @param init The init to register
	 * @throws ParsingException If there is another init with the same name
	 */
	public void registerInit(InitBuilder init) throws ParsingException {
		
		// Don't allow multiple inits with the same name/id
		for (InitBuilder current : inits) {
			if (current.getName().equals(init.getName())) {
				throw new ParsingException("Multiple inits share the name " + init.getName());
			}
		}
		
		inits.add(init);
	}
	
	/**
	 * Registers a main to this application. All mains must be registered here and the programmer will have
	 * to select which of the mains will be used as the start of the application. The other mains can be used
	 * like normal functions. Multiple mains with the same name are not allowed.
	 * @param main The main to register
	 * @throws ParsingException If there is another main with the same name
	 */
	public void registerMain(MainBuilder main) throws ParsingException {
		
		// Don't allow multiple mains with the same name/id
		for (MainBuilder current : mains) {
			if (current.getName().equals(main.getName())) {
				throw new ParsingException("Multiple mains share the name " + main.getName());
			}
		}
		
		mains.add(main);
	}
}