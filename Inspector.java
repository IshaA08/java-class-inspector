import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) throws InstantiationException, IllegalAccessException {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    /**
     * Display information about the given object. Recurse up inheritance if
     * boolean recursive argument is true.
     *
     * @param c Class of the given Object obj
     * @param obj Object that will be analyzed
     * @param recursive boolean value that decides if we recurse up inheritance
     * @param depth integer value keeping track of where we are in inheritance tree
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void inspectClass (Class c, Object obj, boolean recursive, int depth) throws InstantiationException, IllegalAccessException {
      // Print the name of the declaring class
      printClassInformation(c, depth);

      // If there is a superclass, immediately explore it after printing information about it
      printDepthLevel(depth);
      System.out.println("SUPERCLASS -> Recursively Inspect");

      // Print super class information
      printSuperClassInformation(c, depth);
      if (c.getSuperclass() != null) { inspectClass(c.getSuperclass(), obj, recursive, depth + 1); }

      // If there are interfaces, immediately explore them
      printDepthLevel(depth);
      System.out.println("--- INTERFACES ( " + c.getName() + " ) ---");
      printDepthLevel(depth);
      System.out.println("INTERFACES -> Recursively Inspect");
      Class[] interfaces = c.getInterfaces();
      if (interfaces.length == 0) {printDepthLevel(depth); System.out.println("Interfaces: NONE");}
      else {
        // Recursively explore each interface
        for (int i = 0; i < interfaces.length; i++) {
          inspectClass(interfaces[i], obj, recursive, depth + 1);
        }
      }

      // Display the constructors
      displayConstructors(c, obj, depth);

      // Display information on methods
      displayMethods(c, obj, depth);

      // Display fields
      displayFields(c, obj, recursive, depth);
    }

    /**
     * Print basic class information from a given Class
     */
    private void printClassInformation (Class c, int depth) {
      printDepthLevel(depth); System.out.println("--- CLASS ---");
      printDepthLevel(depth); System.out.println("Class Name: " + c.getName());
    }

    /**
     * Print basic super class information from a given Class
     */
    private void printSuperClassInformation (Class obj, int depth) {
      Class superClass = obj.getSuperclass();

      // Check and display if the given object has a super class
      if (superClass == null) {printDepthLevel(depth); System.out.println("SuperClass: NONE");}
      else {printDepthLevel(depth); System.out.println("SuperClass: " + superClass.getName());}

    }

    /**
     * Print spaces pre-appended to standard output for formatting purposes.
     *
     * @param depth int indicating the depth to print spaces for
     */
    private void printDepthLevel(int depth) {
      for (int i = 0; i < depth; i++) { System.out.print("\s\s\s");}
    }

    /**
     * Given a Class, print information about its constructors to standard output
     */
    private void displayConstructors (Class c, Object obj, int depth) {
      printDepthLevel(depth);
      System.out.println("--- CONSTRUCTORS ( " + c.getName() + " ) ---");

      // Find the number of constructors
      Constructor[] constructors = c.getDeclaredConstructors();

      // Check if there is at least one constructor
      if (constructors.length == 0) {printDepthLevel(depth); System.out.println("Constructors -> NONE");}
      // Iterate through all constructors
      else {
        printDepthLevel(depth);
        System.out.println("Constructors ->");

        for (int i = 0; i < constructors.length; i++) {
          printDepthLevel(depth + 1);
          System.out.println("-- CONSTRUCTOR --");
          // Display name
          printDepthLevel(depth + 1);
          System.out.println("Name: " + constructors[i].getName());
          // Display thrown exceptions, parameters and modifiers
          displayExceptions(constructors[i].getExceptionTypes(), depth);
          displayParameters(constructors[i].getParameterTypes(), depth);
          displayModifiers(constructors[i].getModifiers(), depth);
        }
      }

    }

    /**
     * Display the methods of a given Class
     */
    private void displayMethods (Class c, Object obj, int depth) {
      printDepthLevel(depth);
      System.out.println("--- METHODS ( " + c.getName() + " ) ---");

      // Find the number of methods
      Method[] methods = c.getDeclaredMethods();

      // Check if there is at least one method
      if (methods.length == 0) {printDepthLevel(depth); System.out.println("Methods -> NONE");}
      // Iterate through all methods
      else {
        printDepthLevel(depth);
        System.out.println("Methods ->");

        for (int i = 0; i < methods.length; i++) {
          printDepthLevel(depth + 1);
          System.out.println("-- METHOD --");
          // Display name
          printDepthLevel(depth + 1);
          System.out.println("Name: " + methods[i].getName());
          // Display thrown exceptions, parameters and modifiers
          displayExceptions(methods[i].getExceptionTypes(), depth);
          displayParameters(methods[i].getParameterTypes(), depth);
          displayReturnType(methods[i].getReturnType(), depth);
          displayModifiers(methods[i].getModifiers(), depth);
        }
      }
    }

    /**
     * Display the fields found in a given Class
     * Handle each field in the Class differently depending on if it is an array, object reference or primitive
     */
    private void displayFields (Class c, Object obj, boolean recursive, int depth) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, InstantiationException, IllegalAccessException {
      printDepthLevel(depth);
      System.out.println("--- FIELDS ( " + c.getName() + " ) ---");

      // Find the number of fields
      Field[] fields = c.getDeclaredFields();

      // Check if there is at least one field
      if (fields.length == 0) {printDepthLevel(depth); System.out.println("Fields -> NONE");}
      // Iterate through all fields
      else {
        printDepthLevel(depth);
        System.out.println("Fields ->");
        // Deal with each field individually
        for (int i = 0; i < fields.length; i++) {
          fields[i].setAccessible(true);
          // Check if the current field is an array
          if (fields[i].getType().isArray()) { displayArray(fields[i], depth, obj); }
          // Check if the current field references an object
          else if (!fields[i].getType().isPrimitive()) { displayObjectField(fields[i], recursive, depth, obj); }
          else { displayField(fields[i], depth, obj); }
        }
      }
    }

    /**
     * Display information on a single given field
     */
    private void displayField (Field field, int depth, Object obj) throws IllegalArgumentException, IllegalAccessException {
      printDepthLevel(depth + 1);
      System.out.println("-- FIELD --");
      // Display name
      printDepthLevel(depth + 1);
      System.out.println("Name: " + field.getName());
      // Display type
      printDepthLevel(depth + 1);
      System.out.println("Type: " + field.getType().getName());
      // Display value
      printDepthLevel(depth + 1);
      Object fieldValue = null;
      fieldValue = field.get(obj);
      System.out.println("Value: " + fieldValue);
      // Display modifiers
      displayModifiers(field.getModifiers(), depth);
    }

    /**
     * Display information on a given array-type field
     */
    private void displayArray (Field givenField, int depth, Object obj) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, InstantiationException, IllegalAccessException {
      Class field = givenField.getType();
      printDepthLevel(depth + 1);
      System.out.println("-- FIELD --");
      // Display name
      printDepthLevel(depth + 1);
      System.out.println("Name: " + givenField.getName());
      // Display type
      printDepthLevel(depth + 1);
      System.out.println("Type: " + field.getName());
      // Display modifiers
      displayModifiers(givenField.getModifiers(), depth);
      // Display component type
      printDepthLevel(depth + 1);
      System.out.println("Component Type: " + field.getComponentType());
      // Display length
      printDepthLevel(depth + 1);
      Object fieldArray = null;
      fieldArray = givenField.get(obj);
      System.out.println("Length: " + Array.getLength(fieldArray));
      // Display all entries
      printDepthLevel(depth + 1);
      System.out.println("Entries ->");
      for (int i = 0; i < Array.getLength(fieldArray); i++) {
        printDepthLevel(depth + 2);
        System.out.println("Value: " + Array.get(fieldArray, i));
      }
    }

    /**
     * Display information on an object-reference field
     */
    private void displayObjectField (Field field, boolean recursive, int depth, Object obj) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
      printDepthLevel(depth + 1);
      System.out.println("-- FIELD --");
      // Display name
      printDepthLevel(depth + 1);
      System.out.println("Name: " + field.getName());
      // Display type
      printDepthLevel(depth + 1);
      System.out.println("Type: " + field.getType().getName());
      // Display modifiers
      displayModifiers(field.getModifiers(), depth);
      // Display value if recursive is false
      if (!recursive) {
          printDepthLevel(depth + 1);
          System.out.println("Value (ref): " + field.getClass().getName() + "@" + Integer.toHexString(hashCode()));
      }
      // Inspect the object if recursive is true
      else {
        printDepthLevel(depth + 1);
        System.out.println("-> Recursively Inspect");
        // Check if the field is abstract
        if (Modifier.isAbstract(field.getType().getModifiers())) {
        	inspectClass(field.getType(), obj, true, depth + 1);
        }
        else {
        	inspectClass(field.getType(), field.getType().newInstance(), true, depth + 1);
        }
      }
    }

    /**
     * Display the return types of a given Class
     */
    private void displayReturnType(Class c, int depth) {
      printDepthLevel(depth + 1);
      System.out.println("Return Type: " + c.getName());
    }

    /**
     * Display the exceptions of a given Class
     */
    private void displayExceptions (Class[] exceptions, int depth) {
      if (exceptions.length == 0) {printDepthLevel(depth + 1); System.out.println("Exceptions -> NONE");}
      else {
        printDepthLevel(depth + 1);
        System.out.println("Exceptions ->");
        // Iterate through all exceptions
        for (int j = 0; j < exceptions.length; j ++) {printDepthLevel(depth + 2); System.out.println("class " + exceptions[j].getName());}
      }
    }

    /**
     * Display information abouot the parameters of a Class
     */
    private void displayParameters (Class[] params, int depth) {
      if (params.length == 0) {printDepthLevel(depth + 1); System.out.println("Parameter Types -> NONE");}
      else {
        printDepthLevel(depth + 1);
        System.out.println("Parameter Types ->");
        // Iterate through all exceptions
        for (int j = 0; j < params.length; j++) {printDepthLevel(depth + 2); System.out.println(params[j].getName());}
      }
    }

    /**
     * Display the given modifiers
     */
    private void displayModifiers (int modifiers, int depth) {
      printDepthLevel(depth + 1);
      System.out.println("Modifiers: " + Modifier.toString(modifiers));
    }
}
