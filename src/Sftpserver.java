import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class Sftpserver {

	public static void main(String[] args) {

		double p = Double.parseDouble(args[2]);

		// declarations
		String FileName = args[1];
		int port = Integer.parseInt(args[0]);
		DatagramSocket socket = null;
		DatagramSocket ackSocket = null;
		byte[] buffer = new byte[1008];
		byte[] receivingBuffer = new byte[1000];
		byte[] ackBuffer = new byte[8];
		DatagramPacket packet = null;
		DatagramPacket ackPacket = null;
		OutputStream os = null;
		File newFile = new File(FileName);
		int ackNo;
		InetAddress address = null;
		ByteBuffer ackBuf = ByteBuffer.allocate(8);
		int expectedSeqNo = 0;
		int ackID = 0;
		
		try {
			socket = new DatagramSocket(port);
			ackSocket = new DatagramSocket();
			address = InetAddress.getByName("localhost");
			packet = new DatagramPacket(buffer, buffer.length);
			os = new FileOutputStream(newFile, true);
			
			while (true) {
				
				socket.setSoTimeout(10000);
				socket.receive(packet);
				try {

					buffer = packet.getData();
					ByteBuffer buf = ByteBuffer.allocate(buffer.length);
					buf.put(buffer);
					int seqno = buf.getInt(0);
					buf.position(8);
					buf.get(receivingBuffer);
					//int length = (int) newFile.length();
					
					// Send ack only if p > r
					double r = Math.random();

					// If r<=p, packet is discarded
					if (r <= p) {
						System.out.println("Packet Loss, Sequence number: "+seqno);
						
					}else {
						//Check for ACK loss
						if(seqno == expectedSeqNo -1){
							//System.out.println("Hack to handle ACK loss, resending ACK for "+seqno);
							ackBuf.putInt(0, seqno);
							ackBuf.putInt(4, ackID);
							ackBuffer = ackBuf.array();
							ackPacket = new DatagramPacket(ackBuffer, 0, 8,address, 7736);
							ackSocket.send(ackPacket);
						}
						
						// Write to file
						if (expectedSeqNo == seqno)
						{
						os.write(packet.getData(), 8, packet.getLength() - 8);
						expectedSeqNo = expectedSeqNo+1;					
					//	System.out.println("Sequence number" + seqno);
					//	System.out.println("Offset:" + length);
					//	System.out.println("Length" + (packet.getLength() - 4));
						ackNo = seqno;
						ackBuf.putInt(0, ackNo);
						ackBuf.putInt(4,ackID);
						ackBuffer = ackBuf.array();
						ackPacket = new DatagramPacket(ackBuffer, 0, 8,address, 7736);
						ackSocket.send(ackPacket);
						os.flush();
						}else{
							//System.out.println("Packet discarded:"+seqno);
						}
					}
					
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(SocketTimeoutException e)
		{
			System.out.println("Socket timed out.. Closing connection.. End of receiving file");
		}catch (IOException e) {
			System.out.println("Closed");
			try {
				os.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		finally{
			socket.close();
			System.exit(1);
		}
	}
}