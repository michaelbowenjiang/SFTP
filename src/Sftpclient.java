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
		
		int fileSize = (int) sendingFile.length();
		int seqNo = 0;
		int ackNo = 0;
		int MSS;
		MSS = 1000;
		int i = 0, j = 0;
		int size;
		int sendingBufferSize = (int) (fileSize + (4 * Math
				.ceil((float) fileSize / MSS)));
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
			is = new FileInputStream(sendingFile);
			socket = new DatagramSocket();
			address = InetAddress.getByName(hostName);
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
						sendingByteBuffer.put(packetBuffer, 0,
								packetBuffer.length);

						i = i + MSS;
					}
				}
			}
			
			byte[] sendingBuffer = sendingByteBuffer.array();
			System.out.println("Sending Buffer data size "
					+ sendingBuffer.length);

			int beginWindow = 0;
			int endWindow = windowSize * (4 + MSS);
			int windowSeqNo = 0;

			
			sendWindow(sendingBuffer, beginWindow, endWindow, MSS, address, port);
			
			while (endWindow < sendingBuffer.length) {
				try
					{
					
					while( windowSeqNo != seqNo ) {
						
						ackSocket.setSoTimeout(1000);
						ackSocket.receive(ackPacket);
						ackBuffer = ackPacket.getData();
						ackByteBuffer.put(ackBuffer);
						ackByteBuffer.rewind();
						ackNo = ackByteBuffer.getInt(0);
						System.out.println("Ack Received: "+ackNo);
						if(ackNo == windowSeqNo)
						{
						if (sendingBuffer.length - endWindow > (MSS + 4))
						{
						packet = new DatagramPacket(sendingBuffer, endWindow,
								MSS + 4, address, port);
						beginWindow = beginWindow + MSS + 4;
						endWindow = endWindow + MSS + 4;
						windowSeqNo = windowSeqNo + 1;
						
						socket.send(packet);
						}
						else if (endWindow < sendingBuffer.length)
						{
							packet = new DatagramPacket(sendingBuffer, endWindow,
									sendingBuffer.length - endWindow, address, port);
							
							endWindow = sendingBuffer.length;
							socket.send(packet);
						}
					}
						else
						{
							System.out.println("Packet loss: "+windowSeqNo);
						}
					}
					}catch (SocketTimeoutException e) {
				        System.out.println("Timeout Occured for packet"+(ackNo+1));
				        System.out.println("Begin Window Postiion"+ beginWindow);
				        System.out.println("End Window Position"+endWindow);
				        sendWindow(sendingBuffer, beginWindow, endWindow, MSS, address, port);
				        continue;
				    }
				} 
			

			System.out.println("Client done!");
			is.close();
			socket.close();
			ackSocket.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	static void sendWindow(byte[] sendingBuffer, int beginOffset, int endOffset, int MSS, InetAddress address, int port)
	{
		int temp = beginOffset;
		DatagramPacket packet = null;
		
		while(temp<endOffset)
		{
			packet = new DatagramPacket(sendingBuffer, temp, MSS + 4, address,
					port);
			try {
				DatagramSocket socket = new DatagramSocket();
				socket.send(packet);
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Packet Sent:");
			temp += MSS + 4;
		}
		
	}
}