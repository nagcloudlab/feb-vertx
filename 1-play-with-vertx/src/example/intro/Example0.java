package example.intro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

// Vertx - Echo server

public class Example0 {

	public static void main(String[] args) throws Throwable {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(3000));
		while (true) {
			Socket socket = server.accept();
			new Thread(clientHandler(socket)).start();
		}
	}

	private static Runnable clientHandler(Socket socket) {
		return () -> {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				 PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {
				String line = "";
				while (!"/quit".equals(line)) {
					line = reader.readLine();
					System.out.println("~ " + line);
					writer.write(line + "\n");
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
	}

}
