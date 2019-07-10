package arrayscript.lang.element;

import arrayscript.lang.Modifier;

/**
 * Represent a kind of element of the language. More explanation about each type can be found on the
 * constants of the ElementTypes class (yes, but an 's' at the end).
 * @author knokko
 *
 */
public abstract class ElementType {
	
	private ElementType(){}
	
	/**
	 * Determines whether or not elements of this element type can have the given modifier.
	 * @param modifier The modifier
	 * @return true if elements of this type can have the given modifier, false if not
	 */
	public abstract boolean canHave(Modifier modifier);
	
	/**
	 * @return The simple name of this element type
	 */
	public abstract String getName();
	
	/**
	 * Determines whether or not elements of this element type should have a name.
	 * @return true if elements of this type should have a name, false if not
	 */
	public abstract boolean needsName();
	
	/**
	 * Determines whether or not the name of this element type should literally appear in the source code.
	 * If the name should not appear in the source code, programmers are free to use it for custom element
	 * names and variable names.
	 * @return true if the name of this type should have special treatment in the source code, false if not
	 */
	public abstract boolean shouldAppearInSource();
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static class Namespace extends ElementType {
		
		Namespace(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.OPEN || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "namespace";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Class extends ElementType {
		
		Class(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.CONST || modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "class";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Interface extends ElementType {
		
		Interface(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.CONST || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "interface";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Enum extends ElementType {
		
		Enum(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "enum";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Variable extends ElementType {
		
		Variable(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.DEFINE || modifier == Modifier.CONST || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "variable";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			// The type of the variable should be used instead
			return false;
		}
	}
	
	public static class Property extends ElementType {
		
		Property(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.STATIC || modifier == Modifier.CONST || modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "property";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			// The type of the property should be used instead
			return false;
		}
	}
	
	public static class Function extends ElementType {
		
		Function(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "function";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			// The return type should be used instead
			return false;
		}
	}
	
	public static class Method extends ElementType {
		
		Method(){}

		@Override
		public boolean canHave(Modifier modifier) {
			// Don't allow static because the static modifier is a bit fake
			// 'Methods' will the static modifier will be turned into a function
			return modifier == Modifier.CONST || modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "method";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			// The return type should be used instead
			return false;
		}
	}
	
	public static class Constructor extends ElementType {
		
		Constructor(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.IMPLICIT || modifier == Modifier.DEFINE || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "constructor";
		}

		@Override
		public boolean needsName() {
			return false;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Getter extends ElementType {
		
		Getter(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.DEFINE || modifier == Modifier.CONST || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "getter";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Setter extends ElementType {
		
		Setter(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return modifier == Modifier.DEFINE || modifier == Modifier.CONST 
					|| modifier == Modifier.IMPLICIT || modifier.isVisibility();
		}

		@Override
		public String getName() {
			return "setter";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Init extends ElementType {
		
		Init(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return false;
		}

		@Override
		public String getName() {
			return "init";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
	
	public static class Main extends ElementType {
		
		Main(){}

		@Override
		public boolean canHave(Modifier modifier) {
			return false;
		}

		@Override
		public String getName() {
			return "main";
		}

		@Override
		public boolean needsName() {
			return true;
		}

		@Override
		public boolean shouldAppearInSource() {
			return true;
		}
	}
}