import java.util.Scanner;

public class UdpClientStartup {

	public static void main(String[] args) throws Exception {
		UdpClient client = new UdpClient("127.0.0.1", 9000);
		
		Scanner scanner = new Scanner(System.in);
		
		boolean startingClient = true;
		String message = "";
		
		boolean isFirstMessage = true;
		
		while (startingClient) {
			if (isFirstMessage) {
				client.sendData(message, true);
				isFirstMessage = false;
				continue;
			}
			
			System.out.println("Type a message: \r\n");
			message = scanner.nextLine();
			client.sendData(message, false);
			
			if (message.equalsIgnoreCase("EXIT")) {
				startingClient = false;
				break;
			}
			
			System.out.println(client.receiveData());
		}
		
		
		scanner.close();
		
	}
	
}
