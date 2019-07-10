package arrayscript.lang;

import arrayscript.lang.element.Namespace;
import arrayscript.util.Checks;

/**
 * Stores an entire ArrayScript application, instances of this object will be passed to the compiler and
 * created by the parser.
 * @author knokko
 *
 */
public class Application {
	
	private final Namespace globalNamespace;
	
	public Application(Namespace globalNamespace) {
		Checks.notNull(globalNamespace, "globalNamespace");
		this.globalNamespace = globalNamespace;
	}
	
	public Namespace getGlobalNamespace() {
		return globalNamespace;
	}
}