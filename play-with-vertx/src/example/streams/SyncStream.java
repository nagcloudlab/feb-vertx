package example.streams;

import java.io.File;
import java.io.FileInputStream;

public class SyncStream {

	public static void main(String[] args) {

		// 

		File file = new File("Menu.txt");
		byte[] buffer = new byte[1024];

		// stream
		try (FileInputStream in = new FileInputStream(file)) {
			int count = in.read(buffer); // pull
			while (count != -1) {
				System.out.println(new String(buffer, 0, count));
				count = in.read(buffer); // pull
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\n--- DONE");
		}

	}

}

/*
 * problems
 * ----------
 * 
 *  
 *  
 *     ==> blocking IO ( using sync-api lead to code in imperative style )
 *     ==> regular code & error handling code mixed 
 *     
 *     
 * 	  
 */
