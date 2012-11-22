import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Sftpclient {

	public static void main(String[] args) {

		// Declarations
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		String FileName = args[2];
		File sendingFile = new File(FileName);
		InputStream is = null;
		;

		int fileSize = (int) sendingFile.length();
		int seqNo = 0;
		int ackNo = 0;
		int MSS;
		MSS = 1000;
		int i = 0, j = 0;
		int size;
		int sendingBufferSize = (int) (fileSize + (4 * Math.ceil((float)fileSize / MSS)));
		System.out.println(sendingBufferSize);
		
		int windowSize = 5;

		byte[] dataBuffer = null;
		byte[] ackBuffer = new byte[4];
		byte[] packetBuffer = null;

		ByteBuffer ackByteBuffer = ByteBuffer.allocate(4);
		ByteBuffer sendingByteBuffer = ByteBuffer.allocate(sendingBufferSize);
		dataBuffer = new byte[fileSize];

		DatagramSocket socket = null;
		DatagramSocket ackSocket = null;
		DatagramPacket packet = null;
		DatagramPacket ackPacket = new DatagramPacket(ackBuffer,
				ackBuffer.length);
		InetAddress address = null;

		try {
			ackSocket = new DatagramSocket(7736);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			is = new FileInputStream(sendingFile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			socket = new DatagramSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			address = InetAddress.getByName(hostName);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
		try {
			while ((is.read(dataBuffer)) > 0) {
				while (i < dataBuffer.length) {
					if (dataBuffer.length - i > MSS) {

						size = MSS;

						makePacket m = new makePacket(dataBuffer, i, size,
								seqNo);
						packetBuffer = m.getPacket();
						sendingByteBuffer.put(packetBuffer);
						i = i + MSS;
						seqNo = seqNo + 1;
					} else {
						size = dataBuffer.length - i;
						makePacket m = new makePacket(dataBuffer, i, size,
								seqNo);

						packetBuffer = m.getPacket();
						sendingByteBuffer.put(packetBuffer,0,packetBuffer.length);

						i = i + MSS;
					}
				}
			}

			
			
			byte[] sendingBuffer = sendingByteBuffer.array();
			System.out.println("Sending Buffer data size "
					+ sendingBuffer.length);

			int beginWindow = 0;
			int endWindow = windowSize * (4 + MSS);
			seqNo = 0;

			while (j < endWindow) {
				packet = new DatagramPacket(sendingBuffer, j, MSS + 4, address,
						port);
				socket.send(packet);
				j += MSS + 4;
			}

			while (endWindow < sendingBuffer.length) {
				if(sendingBuffer.length - endWindow > (MSS+4))
				{
				ackSocket.receive(ackPacket);
				ackBuffer = ackPacket.getData();
				ackByteBuffer.put(ackBuffer);
				ackByteBuffer.rewind();
				ackNo = ackByteBuffer.getInt(0);
				if (ackNo == seqNo) {
					seqNo = seqNo + 1;
					beginWindow = beginWindow + MSS + 4;

					packet = new DatagramPacket(sendingBuffer, endWindow,
							MSS + 4, address, port);
					endWindow = endWindow + MSS + 4;
					socket.send(packet);
					continue;
				} else {
					System.out.println("Packet loss");

				}
				}
				else
				{
					packet = new DatagramPacket(sendingBuffer, endWindow,
							sendingBuffer.length - endWindow, address, port);
					endWindow = endWindow + MSS + 4;
					socket.send(packet);
					
				}
			}

			System.out.println("Client done!");
			is.close();
			// socket.close();
			// ackSocket.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
