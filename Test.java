
public class Test {
	
	public static void main(String[] args) {
		ClassB b;
		ClassA a;
		ClassD d;
		try {
			d = new ClassD();
			a = new ClassA();
			b = new ClassB();
			Inspector in = new Inspector();
			in.inspect(d, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
