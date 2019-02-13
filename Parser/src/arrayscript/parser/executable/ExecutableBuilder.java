package arrayscript.parser.executable;

import arrayscript.lang.element.Namespace;
import arrayscript.lang.executable.Executable;
import arrayscript.parser.builder.AppBuilder;

public class ExecutableBuilder {
	
	private final String unparsedExecutable;
	private Executable executable;
	
	public ExecutableBuilder(String unparsed) {
		unparsedExecutable = unparsed;
	}
	
	public boolean isConfirmed() {
		return executable != null;
	}
	
	public void parse(AppBuilder application, Namespace namespace) {
		// TODO parse everything correctly
	}
	
	public Executable getExecutable() {
		if (executable == null) {
			throw new IllegalStateException("Executable is not yet parsed");
		}
		
		return executable;
	}
}