package arrayscript.parser.builder;

import arrayscript.util.Checks;

public class ImportBuilder {
	
	private final String[] parts;
	
	private ElementBuilder target;
	
	public ImportBuilder(String[] parts) {
		Checks.noNull(parts);
		if (parts.length == 0) {
			throw new IllegalArgumentException("Import parts can't be empty");
		}
		this.parts = parts;
	}
	
	/**
	 * This method will return the target of the import if it already has been resolved. If it is not yet
	 * resolved, an IllegalStateException will be thrown.
	 * @return The target of the import (what the import refers to)
	 * @throws IllegalStateException If this import is not yet resolved
	 */
	public ElementBuilder getTarget() throws IllegalStateException {
		if (target == null) {
			throw new IllegalStateException("Target has not yet been resolved");
		}
		return target;
	}
	
	/**
	 * Gets the name of this import. The name of the import is the last part of the import, which is how
	 * imports are referred to in the source code.
	 * @return The name/last part of this import
	 */
	public String getName() {
		return parts[parts.length - 1];
	}
}