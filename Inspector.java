//CPSC 501 Assignment 2, Brandon Sin, 30012020
//Reference to:
//http://www.java2s.com/Tutorial/Java/0125__Reflection/Recursivelygetallfieldsforahierarchicalclasstree.htm
//https://stackoverflow.com/questions/22031207/find-all-classes-and-interfaces-a-class-extends-or-implements-recursively

import java.lang.reflect.*;
import java.util.*;


public class Inspector {
	static int level;
	static int n = 0;
	static List<Class> superclassArray = new ArrayList<Class>();


//This method will introspect on the object specified by the first parameter, printing what it finds to standard output.
	
	public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);

	}
	
	
	
	public static Field[] getAllSuperclass(Class cl) {
		List<Field> superClasses = new ArrayList<Field>();
		superClasses.addAll(Arrays.asList(cl.getDeclaredFields()));
		if (cl.getSuperclass() != null) {
			superClasses.addAll(Arrays.asList(getAllSuperclass(cl.getSuperclass())));
		}
	
		return superClasses.toArray(new Field[] {});
		
		
		
	}
	
	//local Class for finding Interfaces recursively
	//Entity is Collection of Arrays
	//takes in Class cl
	public static Collection<? extends Class> getInterface(Class cl) {
		//Initializing Array for Interface Method
		List<Class> interfaceArray = new ArrayList<Class>();
		level = 0;
		
		//check java.lang.object when no superclass persists
		while(!"java.lang.Object".equals(cl.getName())) {
			Class[] inter = cl.getInterfaces();
			
			//if condition to check if class has interfaces
			if(inter.length > 0) {
				
			//if condition is true then we combine both list of interfaces together
				interfaceArray.addAll(Arrays.asList(inter));
				level+=1;
				for (Class interfac : inter) {
					superclassArray.addAll(Arrays.asList(cl.getSuperclass()));
					interfaceArray.addAll(getInterface(interfac));
				}
			}
			Class<?> superClass = cl.getSuperclass();
			
			//when superclass does not exist break out while loop
			if(superClass == null) {
				break;
			}
			//set cl as the next superclass for the next scanning
			cl = superClass;
			
		}
	
		
		return interfaceArray;
		
	}
	
	//Local Class for finding superclasses recursively
	//entity of Class[]
	//takes in Class cl
	public static Class[] getSuperClasses(Class cl) {
		
		//initialization of arraylist for superlcass
		List<Class> superClass = new ArrayList<Class>();
		
		//if condition for checking if class contains superclass
		if (cl.getSuperclass() != null){
			
			//combine lists and recursion
			superClass.addAll(Arrays.asList(cl.getSuperclass()));
			superClass.addAll(Arrays.asList(getSuperClasses(cl.getSuperclass())));
		}
		return superClass.toArray(new Class[] {});

	}
	
	//Method for inspecting methods 
	//returns void
	//takes in Class c
	public void getMethods(Class c) {
		Method[] method = c.getDeclaredMethods();
		System.out.println("Method:");
		for(Method me : method) {
			System.out.println("\tName:" + me.getName());
			
			Class[] ex = me.getExceptionTypes();
			if(ex.length == 0) {
				System.out.println("\tNo Exception");
			}
			for(Class exception : ex) {
				System.out.println("\tException Type:" + exception.getName());
			}
			Class[] pTypes = me.getParameterTypes();
			if(pTypes.length ==0){ 
				System.out.println("\tNo Parameters");
			}
				
			else {
				for(Class pp : pTypes) {	
					System.out.println("\tParameter:" + pp.getName());
				}
			}
			System.out.println("\tReturn Type: " + me.getReturnType());
			System.out.println("\tModifier(s): " + Modifier.toString(me.getModifiers()) + "\n");
		}
		
		
	}
	public void getConstructors(Class c) {
		System.out.println("\nConstructors:");
		Constructor[] con = c.getConstructors();
		for(Constructor cc : con) {
			System.out.println("\tName: " + cc.getName());
			Class[] pTypes = cc.getParameterTypes();
			if(pTypes.length ==0){ 
				System.out.println("\tParameter Type: None");
			}
			
			else {
				for(Class pp : pTypes) {	
					System.out.println("\tParameter Type: " + pp.getName());
				}
			}
			System.out.println("\tModifier(s):" + Modifier.toString(cc.getModifiers()) + "\n");
		}
	}
	
	
	
	
	
	//method for getting the class name and determining its entity
	//takes in Class c
	//returns nothing
	public void getClass(Class c) {
		
		//if conditions for determining its entity
		if (c.getName() != null) {
			if (c.isPrimitive()) {
				System.out.println("Primitive Class: " + c.getName());
			}
			else if (c.isArray()) {
				System.out.println("Array Class: " + c.getComponentType());
			}
			else if (c.isInterface()) {
				System.out.println("Interface Class: " + c.getName());
			}
			else {
				System.out.println("Ordinary Class: " + c.getName());
			}
		}
		else {
			System.out.println("Not a Class");
		}	
	}
	
	public void boolFields(Field[] classFields, Object obj) {
		for(Field field : classFields) {
			field.setAccessible(true);
			System.out.println("\tName: " + field.getName());
			
			System.out.println("\tType: " + field.getType());
			
			System.out.println("\tModifier(s): " + Modifier.toString(field.getModifiers()));
			try {
				
				if(field.getType().isArray()) {
					
					try {
						Object array = field.get(obj);
						
						int length = Array.getLength(array);
						for(int i = 0; i < length; i++) {
							Object element = Array.get(array, i);
							System.out.println("\tField Value: " + element);
						}
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					System.out.println("\tField Value: " + field.get(obj) + "\n");	
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		
		
		//call getClass
		getClass(c);
		
		
		
		//superClass
		System.out.println("\nSuperclass Name(s): ");
		Class[] superClassName = getSuperClasses(c);
		for(Class sClass : superClassName)
			if(!sClass.getName().equals("java.lang.Object")) {
				if(sClass.isArray()) {
					System.out.println(c.getSuperclass());
					System.out.println("\t" + sClass.getComponentType());
				}
				else {
					System.out.println("\t" + sClass.getName());
				}
			}
			else {
				if(superClassName.length > 1) {
				}
				else {
					System.out.println("\tdoes not have a superclass.");
				}
			}
	
		
		//interface
		Collection<? extends Class> InterName = getInterface(c);
		superclassArray.size();
		if(InterName.size() == 0) {
			System.out.println("\nThere are no Interfaces.");
		}
		else {
			System.out.println("\nInterfaces are/is :");
				for(Class x : InterName) {
					for(int i = 0; i < 1; i++) {
						System.out.println(superclassArray.get(n));
						n++;
						
					}

					System.out.println("\t" + x.getName());
			}
		}
		
		//call getConstructors()
		getConstructors(c);
		
		
		//call getMethods()
		getMethods(c);
		
		//Fields
		System.out.println("\nFields: ");
		if(recursive == false) {
			Field[] classFields = c.getDeclaredFields();
			boolFields(classFields, obj);
		}
		if(recursive == true) {
			Field[] classFields = getAllSuperclass(c);
			boolFields(classFields, obj);
			
		
		}
	}
		
		
		
		
		
	
	
	
}
