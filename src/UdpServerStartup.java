public class UdpServerStartup {

	public static void main(String[] args) throws Exception {
		UdpServer server = new UdpServer(9000);
		server.startServer();	
	}
	
}
