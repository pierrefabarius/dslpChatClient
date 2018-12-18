import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {

	final static String prefix = "dslp/1.2\r\n";
	final static String suffix = "dslp/end\r\n";
	final static String groupNotify = "group notify\r\n";
	final static String peerNotify = "peer notify\r\n";
	final static String groupJoin = "group join\r\n";
	final static String groupLeave = "group leave\r\n";
	final static String connect = ": joined server\r\n";
	final static String disconnect = ": disconnected from server\r\n";
	final static String timeRequest = "request time\r\n";
	final static int port = 44444;
	final static String ip = "localhost";
	static String group = null;

	public static void main(String[] args) {

		try {
			Socket socket = new Socket(ip, port);
			if (socket.isConnected()) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				MyThread thread = new MyThread(in);
				thread.start();

				System.out.println("------------------------------");
				System.out.println("ChatClient v0.4");
				System.out.println("connected to: " + socket.getInetAddress());
				System.out.println("------------------------------");
				System.out.println("commands:");
				System.out.println(".exit");
				System.out.println(".join <group>");
				System.out.println(".leave <group>");
				System.out.println(".notify <group> <message>");
				System.out.println(".peer <peer> <meesage>");
				System.out.println(".time");
				System.out.println(".help");
				System.out.println("------------------------------");

				Scanner scanner = new Scanner(System.in);
				String command = "";

				// out.write(prefix + groupJoin + group + "\r\n" + suffix);
				// out.flush();

				while (command.trim() != null) {
					command = scanner.nextLine();
					if (command.equals(".exit")) {
						break;
					} else if (command.contains(".join")) {
						String s[] = command.split(" ");
						if(s.length == 2){
							group = s[1];
							out.write(prefix + groupJoin + group + "\r\n" + suffix);
							out.flush();
						}
					} else if (command.contains(".leave")) {
						String s[] = command.split(" ");
						if(s.length == 2){
							group = s[1];
							out.write(prefix + groupLeave + group + "\r\n" + suffix);
							out.flush();
							group = null;
						}
					} else if (command.contains(".notify")) {
						String s[] = command.split(" ");
						if(s.length > 1){
							group = s[1];
							String message = "";
							for (int i = 2; i < s.length; i++) {
								message += s[i] + " ";
							}
							message.trim();
							out.write(prefix + groupNotify + group + "\r\n" + message + "\r\n" + suffix);
							out.flush();
						}
					} else if (command.equals(".time")) {
						out.write("dslp/1.2\r\n" + timeRequest + "dslp/end\r\n");
						out.flush();
					} else if (command.contains(".peer")) {
						String s[] = command.split(" ");
						if(s.length > 1){
							String peer = s[1];
							String message = "";
							for (int i = 2; i < s.length; i++) {
								message += s[i] + " ";
							}
							message.trim();
							out.write("dslp/1.2\r\n" + peerNotify + peer + "\r\n" + message + "\r\n" + suffix);
							out.flush();
						}
					}  else if(command.equals(".help")){
						System.out.println("------------------------------");
						System.out.println("commands:");
						System.out.println(".exit");
						System.out.println(".join <group>");
						System.out.println(".leave <group>");
						System.out.println(".notify <group> <message>");
						System.out.println(".peer <peer> <meesage>");
						System.out.println(".time");
						System.out.println("------------------------------");
					} else {
						System.out.println("unknown command");
					}
				}
				thread.kill();
				in.close();
				out.close();
				socket.close();
				scanner.close();

			}
		} catch (IOException e) {
			System.out.println("error: " + e.getMessage());
		}
	}
}
