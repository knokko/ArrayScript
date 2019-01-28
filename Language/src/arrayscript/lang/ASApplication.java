package arrayscript.lang;

import arrayscript.util.Checks;

/**
 * Stores an entire ArrayScript application, instances of this object will be passed to the compiler and
 * created by the parser.
 * @author knokko
 *
 */
public class ASApplication {
	
	private final ASNamespace globalNamespace;
	
	public ASApplication(ASNamespace globalNamespace) {
		Checks.notNull(globalNamespace, "globalNamespace");
		this.globalNamespace = globalNamespace;
	}
	
	public ASNamespace getGlobalNamespace() {
		return globalNamespace;
	}
}