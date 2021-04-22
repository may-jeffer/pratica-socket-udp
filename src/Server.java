import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static List<InetAddress> address = new ArrayList<>();
	private static List<Integer> ports = new ArrayList<>();

	public static void main(String[] args) throws Exception {

		try (DatagramSocket s = new DatagramSocket(4545)) {
			System.out.println("Start do servidor feito com sucesso!");

			while (true) {
				DatagramPacket recebe = new DatagramPacket(new byte[512], 512);
				s.receive(recebe);
				String recebido = new String(recebe.getData());

				if (recebido.trim().equalsIgnoreCase("Startar")) {
					address.add(recebe.getAddress());
					ports.add(recebe.getPort());
				} else {
					sendAll(s, recebe);			
					System.out.println(recebido);
				}
			}
		}
	}

	private static void sendAll(DatagramSocket socket, DatagramPacket recebido) throws IOException {
		for (int i = 0; i < address.size(); i++) {
			DatagramPacket resp = new DatagramPacket(recebido.getData(), recebido.getLength(), address.get(i),
					ports.get(i));
			socket.send(resp);
		}
	}
}
