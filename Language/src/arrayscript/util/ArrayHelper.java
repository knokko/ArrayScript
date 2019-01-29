package arrayscript.util;

import java.util.List;

public class ArrayHelper {
	
	public static <T> T[] fromList(List<T> source, T[] dest) {
		source.toArray(dest);
		return dest;
	}
}