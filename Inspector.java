//CPSC 501 Assignment 2, Brandon Sin, 30012020
//Reference to:
//http://www.java2s.com/Tutorial/Java/0125__Reflection/Recursivelygetallfieldsforahierarchicalclasstree.htm
//https://stackoverflow.com/questions/22031207/find-all-classes-and-interfaces-a-class-extends-or-implements-recursively

import java.lang.reflect.*;
import java.util.*;


public class Inspector {


	
	public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);

	}
	
	
	//Find Fields Recursively
//	public static Field[] getAllSuperclass(Class cl) {
//		List<Field> superClasses = new ArrayList<Field>();
//		superClasses.addAll(Arrays.asList(cl.getDeclaredFields()));
//		if (cl.getSuperclass() != null) {
//			superClasses.addAll(Arrays.asList(getAllSuperclass(cl.getSuperclass())));
//		}
//	
//		return superClasses.toArray(new Field[] {});
//		
//		
//		
//	}
	
	//Find Interface Recursively
	public static Collection<? extends Class> getInterface(Class cl) {
		List<Class> interfaceArray = new ArrayList<Class>();
		Class[] inter = cl.getInterfaces();
		if(inter.length > 0) {
			interfaceArray.addAll(Arrays.asList(inter));
			for (Class interfac : inter) {
				interfaceArray.addAll(getInterface(interfac));
			}
		}
		return interfaceArray;
		
	}
	
	//Find Superclass recursively
	public static Class[] getSuperClasses(Class cl) {
		List<Class> superClass = new ArrayList<Class>();
		
		if (cl.getSuperclass() != null){
			superClass.addAll(Arrays.asList(cl.getSuperclass()));
			superClass.addAll(Arrays.asList(getSuperClasses(cl.getSuperclass())));
		}
			
				
		return superClass.toArray(new Class[] {});

	}

	
	
	private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
		
		
		//Class
		if (c.getName() != null) {
				if (c.isPrimitive()) {
					System.out.println("Primitive Class: " + c.getName());
				}
				if (c.isArray()) {
					System.out.println("Array Class: " + c.getName());
				}
				if (c.isInterface()) {
					System.out.println("Interface Class: " + c.getName());
				}
				else {
					System.out.println("Ordinary Class: " + c.getName());
				}
			}
		else {
				System.out.println("Not a Class");
			}
		
		
		//superClass
		System.out.println("\nSuperclass Name(s): ");
		Class[] superClassName = getSuperClasses(c);
		for(Class sClass : superClassName)
			if(!sClass.getName().equals("java.lang.Object")) {
				System.out.println("\t" + sClass.getName());
				
			}
			else {
				if(superClassName.length > 1) {
				}
				else {
					System.out.println("\t" +c.getName() + " does not have a superclass.");
				}
			}
	
		
		//interface
		Collection<? extends Class> InterName = getInterface(c);
		if(InterName.size() == 0) {
			System.out.println("\nThere are no Interfaces.");
		}
		else {
			System.out.println("\nInterfaces are/is :");
			for(Class i : InterName) {
				System.out.println("\t" + i.getName());
			}
		}
		
		//Constructors
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
		//Methods
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
		
		//Fields
		System.out.println("\nFields: ");
		Field[] classFields = c.getDeclaredFields();
		
		for(Field field : classFields) {
			field.setAccessible(true);
			System.out.println("\tName: " + field.getName());
			
			System.out.println("\tType: " + field.getType());
			System.out.println("\tModifier(s): " + Modifier.toString(field.getModifiers()));
			try {
				System.out.println("\tField Value: " + field.get(obj) + "\n");
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			
		}
	
		
		
		
	}
		
		
		
		
		
	
	
	
}
