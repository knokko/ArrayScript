package arrayscript.parser.source.reading;

import java.io.IOException;
import java.util.Scanner;

import arrayscript.parser.util.ParsingException;

/**
 * The first source file reader in the chain of source readers. The task of this reader is to find all 'words'
 * that are separated by whitespace characters, except for those whitespace characters that are in a string.
 * (So this reader will not break strings.)
 * @author knokko
 *
 */
public class SourceFileReader1 {

	private static boolean isSeparator(char value) {
		return Character.isWhitespace(value);
	}

	private Scanner scanner;
	private String currentLine;
	private int currentIndex;

	private boolean inDoubleString;
	private boolean inSingleString;

	public SourceFileReader1(Scanner fileScanner) {
		scanner = fileScanner;
	}

	private void skipCurrentLineWhitespaces() {
		// Find the next non-whitespace character
		while (currentIndex < currentLine.length() && isSeparator(currentLine.charAt(currentIndex))) {
			currentIndex++;
		}

		if (currentIndex >= currentLine.length()) {

			// End of line has been reached, so try to read the next line in the next if
			// block
			currentLine = null;
		}
	}

	private void skipCurrentFileWhitespaces() throws IOException {
		if (currentLine != null && !inDoubleString && !inSingleString) {
			skipCurrentLineWhitespaces();
		}
		while (scanner != null && currentLine == null) {

			// All strings must be terminated before the end of its line
			if (inDoubleString || inSingleString) {
				throw new IOException("Unterminated string");
			}

			if (scanner.hasNextLine()) {

				// Start at the new line
				currentLine = scanner.nextLine();
				currentIndex = 0;
				skipCurrentLineWhitespaces();
			} else {

				// End of file has been reached
				scanner.close();
				currentLine = null;
				return;
			}
		}
	}

	/**
	 * Reads the next String that is separated by whitespace characters or line endings.
	 * If the end of the current line is reached, it will go on with the next line. As an exception, this
	 * method will not interrupt strings. So if there is a whitespace character within a string, that
	 * whitespace character will be included in the result and the reading will continue. If the end of the
	 * current source file is reached, it will return null.
	 * @return The next String in this source file or null if the end of the file has been reached
	 * @throws IOException If an IO error occurs while reading the source file
	 * @throws ParsingException If the source code is not correct and thus can't be parsed
	 */
	public String next() throws IOException, ParsingException {
		skipCurrentFileWhitespaces();

		if (currentLine == null) {

			// If we reach this, the end of the file has been reached
			return null;
		}

		// By now, the character at the currentIndex should be non-whitespace
		int endIndex = currentIndex;
		char current = endIndex < currentLine.length() ? currentLine.charAt(endIndex) : 0;
		while (endIndex < currentLine.length() && (!isSeparator(current) || inDoubleString || inSingleString)) {
			if (current == '"' && !inSingleString) {
				inDoubleString = !inDoubleString;
			} else if (current == '\'' && !inDoubleString) {
				inSingleString = !inSingleString;
			}
			endIndex++;
			if (endIndex < currentLine.length()) {
				current = currentLine.charAt(endIndex);
			}
		}
		if (endIndex == currentLine.length() && (inSingleString || inDoubleString)) {
			throw new ParsingException("Unterminated string");
		}
		String result = currentLine.substring(currentIndex, endIndex);
		currentIndex = endIndex + 1;
		return result;
	}
}