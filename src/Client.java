import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

public class Client extends Thread {

	private JFrame frame;
	private JTextField message;
	private JTextField name;
	private JTextArea chat;

	private DatagramSocket socket;
	private InetAddress dest;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApplication window = new ClientApplication();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public Client() {
		initialize();
		new Thread(this).start();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel title = new JLabel("Cliente - UDP");
		title.setFont(new Font("Tahoma", Font.PLAIN, 18));
		title.setHorizontalAlignment(SwingConstants.CENTER);

		chat = new JTextArea();
		chat.setEditable(false);

		JButton send = new JButton("Enviar");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});

		message = new JTextField();
		message.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		message.setToolTipText("");
		message.setColumns(10);

		JLabel lblName = new JLabel("Nome:");

		name = new JTextField();
		name.setText("Usuario");
		name.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(title, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(name, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(chat, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
								.addComponent(message, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(send)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(title, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblName))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chat, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
						.addComponent(message, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(send, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
					.addGap(21))
		);
		frame.getContentPane().setLayout(groupLayout);
	}

	@Override
	public void run() {
		try {
			socket = new DatagramSocket();
			dest = InetAddress.getByName("localhost");
			String startConnection = "start";
			DatagramPacket iniciar = new DatagramPacket(startConnection.getBytes(), startConnection.length(), dest,
					4545);
			socket.send(iniciar);

			while (true) {
				DatagramPacket resposta = new DatagramPacket(new byte[512], 512);
				socket.receive(resposta);
				String resp = new String(resposta.getData());
				chat.append(resp + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessage() {
		String nome = (name.getText().trim().isEmpty()) ? "Usuário" : name.getText().trim();
		String mensagem = message.getText().trim();
		if (!mensagem.isEmpty() && mensagem != null) {
			try {
				String envio = String.format("%s: %s", nome, mensagem);
				DatagramPacket msg = new DatagramPacket(envio.getBytes(), envio.length(), dest, 4545);
				socket.send(msg);
				message.setText("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
