import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class MyThread extends Thread {

	final String prefix = "dslp/1.2";
	final String suffix = "dslp/end";
	final String groupNotify = "group notify";
	final String peerNotify = "peer notify";
	final String responseTime = "response time";

	private BufferedReader in;
	public static boolean check = true;

	public MyThread(BufferedReader in) {
		this.in = in;
	}

	public void run() {
		String response = "";
		while (check) {
			try {
				if(in.ready()) {
					response = in.readLine();
					if (response.equals(prefix)) {
						response = in.readLine();
						if (response.equals(groupNotify)) {
							response = in.readLine();
							// if (response.equals(Main.group)) {
								response = in.readLine();
								while (!(response.equals(suffix))) {
									System.out.println(response);
									response = in.readLine();
								// }
							}
						} else if(response.equals(responseTime)){
							response = in.readLine();
							try{
								SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
								System.out.println(time.parse(response));
							} catch (ParseException e){
								System.out.println(e.getMessage());
							}
						} else if(response.equals(peerNotify)){
							response = in.readLine();
							response = in.readLine();
							while (!(response.equals(suffix))) {
								System.out.println(response);
								response = in.readLine();
							}
						} else if(response.equals("error")){
							response = in.readLine();
							while (!(response.equals(suffix))) {
								System.err.println(response);
								response = in.readLine();
							}
						}
					}
				}
				Thread.sleep(300);
			} catch (IOException | InterruptedException e1) {
				System.out.println("error: " + e1.getMessage());
				return;
			}
		}
	}

	public void kill() {
		check = false;
	}

}
