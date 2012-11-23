import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Sftpserver {

	public static void main(String[] args) {

		double p = Double.parseDouble(args[2]);

		// declarations
		String FileName = args[1];
		int port = Integer.parseInt(args[0]);
		DatagramSocket socket = null;
		DatagramSocket ackSocket = null;
		byte[] buffer = new byte[1004];
		byte[] receivingBuffer = new byte[1000];
		byte[] ackBuffer = new byte[4];
		DatagramPacket packet = null;
		DatagramPacket ackPacket = null;
		OutputStream os = null;
		File newFile = new File(FileName);
		int ackNo;
		InetAddress address = null;
		ByteBuffer ackBuf = ByteBuffer.allocate(4);
		int expectedSeqNo = 0;
		
		try {
			socket = new DatagramSocket(port);
			ackSocket = new DatagramSocket();
			address = InetAddress.getByName("localhost");
			packet = new DatagramPacket(buffer, buffer.length);
			
			while (true) {
				
				socket.receive(packet);
				try {

					os = new FileOutputStream(newFile, true);

					buffer = packet.getData();
					ByteBuffer buf = ByteBuffer.allocate(buffer.length);
					buf.put(buffer);
					int seqno = buf.getInt(0);
					buf.position(4);
					buf.get(receivingBuffer);
					int length = (int) newFile.length();

					

					// Send ack only if p > r
					double r = Math.random();

					// If r<=p, packet is discarded
					if (r <= p) {
						System.out.println("Packet Loss! Sequence number:"+seqno);
						
					} else {
						
						// Write to file
						if (expectedSeqNo == seqno)
						{
						os.write(packet.getData(), 4, packet.getLength() - 4);
						expectedSeqNo = expectedSeqNo+1;					
						System.out.println("Sequence number" + seqno);
						System.out.println("Offset:" + length);
						System.out.println("Length" + (packet.getLength() - 4));
						ackNo = seqno;
						ackBuf.putInt(0, ackNo);
						ackBuffer = ackBuf.array();
						ackPacket = new DatagramPacket(ackBuffer, 0, 4,address, 7736);
						ackSocket.send(ackPacket);
						os.close();
						}
					}
					

				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
