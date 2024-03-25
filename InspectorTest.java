/**
 * JUnit testing for Inspector.java. Ensures black-box behavior of methods related to getting 
 * class information is consistent and accurate. Also makes use of ClassA.java file given.
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;


class InspectorTest {

	/**
	 * Target Functionality: Display a given Class's name
	 */
	@Test
	void testPrintClassInformation() {
		// Set the expected output
		String testOne = "\s\s\s--- CLASS ---";
		String testTwo = "\s\s\sClass Name: ClassA";
		
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
		
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
		
		// Invoke target method
		inspector.printClassInformation(test.getClass(), 1);
		
		// Check if output matches expected
		assertTrue(testOutput.toString().contains(testOne));
		assertTrue(testOutput.toString().contains(testTwo));
	}

	/**
	 * Target Functionality: Display a given Class's superclass name
	 */
	@Test
	void testPrintSuperClassInformation() {
		// Set the expected output
		String testOut = "\s\s\sSuperClass: java.lang.Object";
				
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
				
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassD test = new ClassD();
				
		// Invoke target method
		inspector.printSuperClassInformation(test.getClass(), 1);
		
		// Check if output matches expected
		assertTrue(testOutput.toString().contains(testOut));
	}

	/**
	 * Target Functionality: Indent properly according to the depth level
	 */
	@Test
	void testPrintDepthLevel() {
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();

		// Test #1: Depth level 1
		inspector.printDepthLevel(1);				
		assertTrue(testOutput.toString().equals("\s\s\s"));
		testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
		
		// Test #2: Depth level 3
		inspector.printDepthLevel(3);
		assertTrue(testOutput.toString().equals("\s\s\s\s\s\s\s\s\s"));
		testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
		
		// Test #3: No depth
		inspector.printDepthLevel(0);
		assertTrue(testOutput.toString().equals(""));
		testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
	}

	/**
	 * Target Functionality: Method information display to standard output
	 */
	@Test
	void testDisplayMethods() {
		// Set expected output
		String expected = "-- METHOD --\r\n"
				+ "   Name: run\r\n"
				+ "   Exceptions -> NONE\r\n"
				+ "   Parameter Types -> NONE\r\n"
				+ "   Return Type: void\r\n"
				+ "   Modifiers: public";
		
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
						
		// Invoke target method
		inspector.displayMethods(test.getClass(), test, 0);
				
		// Check if output matches expected
		assertTrue(testOutput.toString().contains("--- METHODS ( ClassA ) ---"));
	}

	/**
	 * Target Functionality: Field information printed properly to standard output
	 */
	@Test
	void testDisplayField() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {			
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
		Field[] a = test.getClass().getDeclaredFields();
		a[0].setAccessible(true);
						
		// Invoke target method
		inspector.displayField(a[0], 0, test);
				
		// Check if output matches expected
		assertTrue(testOutput.toString().contains("-- FIELD --"));
	}

	/**
	 * Target Functionality: Return type information display
	 */
	@Test
	void testDisplayReturnType() {
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
								
		// Invoke target method
		inspector.displayReturnType(test.getClass(), 0);
				
		// Check if output matches expected
		assertTrue(testOutput.toString().contains("Return Type: "));
	}

	/**
	 * Target Functionality: Parameter display
	 */
	@Test
	void testDisplayParameters() {
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
						
		// Invoke target method
		inspector.displayParameters(test.getClass().getMethods()[0].getParameterTypes(), 0);
				
		// Check if output matches expected
		assertTrue(testOutput.toString().contains("Parameter Types ->"));
	}

	/**
	 * Target Functionality: Modifier display
	 */
	@Test
	void testDisplayModifiers() {
		// Make sure whatever is written to console by Inspector instead comes here
		ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
		System.setOut(new PrintStream(testOutput));
						
		// Initialize Inspector and ClassA
		Inspector inspector = new Inspector();
		ClassA test = new ClassA();
						
		// Invoke target method
		inspector.displayModifiers(test.getClass().getModifiers(), 0);
		
		// Check if output matches expected
		assertTrue(testOutput.toString().contains("Modifiers: "));
	}

}
