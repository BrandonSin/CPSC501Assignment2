import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class JTest {
	
	public static void main(String[] args) throws Exception {

		
		runTest("test1.txt", new ClassA(100), true );
		runTest("test2.txt", new ClassA(100), false );
		runTest("test3.txt", new ClassB(), true );
		runTest("test4.txt", new ClassB(), false );
		runTest("test5.txt", new ClassD(), true );
		runTest("test6.txt", new ClassD(100), false );
		runTest("test7.txt", new ClassA[5], false );
		runTest("test8.txt", new ClassB[5], false );
		runTest("test9.txt", new ClassD[5], false );
		runTest("test10.txt", 5, false );
	}
	
    public static void runTest(String filename, Object testObj, boolean recursive) {
        try {
            PrintStream old = System.out;
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            System.setOut(ps);
            System.out.println("======================================================");
            System.out.println("Filename: " + filename);
            System.out.println("Running Test: " + testObj);
            System.out.println("Recursive: " + recursive);
            new Inspector().inspect(testObj, recursive);
            System.out.println("======================================================");
            ps.flush();
            fos.flush();
            ps.close();
            fos.close();
            System.setOut(old);
        } catch (IOException ioe) {
            System.err.println("Unable to open file: " + filename);
        } catch (Exception e) {
            System.err.println("Unable to compleatly run test: " + testObj);
            e.printStackTrace();
        }
    }
}


