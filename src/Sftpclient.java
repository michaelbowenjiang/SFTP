import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
public class Sftpclient {

	public static void main(String[] args){
		
		//Declarations
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		String FileName = args[2];
		File sendingFile = new File(FileName);
		InputStream is = null;;
		
		
		byte[] dataBuffer = null;
		byte[] ackBuffer = new byte[4];
		byte[] packetBuffer = null;
		
		ByteBuffer ackByteBuffer = ByteBuffer.allocate(4);
		dataBuffer = new byte[(int)sendingFile.length()];
		
		DatagramSocket socket = null;
		DatagramSocket ackSocket = null;
        DatagramPacket packet = null;
        DatagramPacket ackPacket = new DatagramPacket(ackBuffer, ackBuffer.length);
        InetAddress address = null;
        
        
        int seqNo=0;
        int ackNo=0;
        int MSS;
        MSS = 1000;
        int i = 0;
		int size;
	//	byte[] sendingBuffer = null;
		
        
       
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
			}catch (IOException e) {
			e.printStackTrace();
		}
		try {
			address = InetAddress.getByName(hostName);
			} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	try {
				while((is.read(dataBuffer))>0)
				{
				while(i<dataBuffer.length)
				{
					if(dataBuffer.length - i > MSS)
					{
						
						size = MSS;
						
						makePacket m = new makePacket(dataBuffer, i, size, seqNo);
						System.out.println("Offset"+i);
						packetBuffer = m.getPacket();
						packet = new DatagramPacket(packetBuffer, 0, packetBuffer.length, address, port);
						System.out.println("Packet data size"+size);
						
						
						socket.send(packet);
						
						ackSocket.receive(ackPacket);
						ackBuffer = ackPacket.getData();
						ackByteBuffer.put(ackBuffer);
						ackByteBuffer.rewind();
						ackNo = ackByteBuffer.getInt(0);
						if (ackNo == seqNo)
						{
							i = i+MSS;
							seqNo = seqNo+1;
							continue;
						}
						else
						{
							System.out.println("Packet loss");
							break;
						}
					}
					else
					{	
						size= dataBuffer.length - i;
						makePacket m = new makePacket(dataBuffer, i, size, seqNo);
						
						packetBuffer = m.getPacket();
						packet = new DatagramPacket(packetBuffer, 0, packetBuffer.length, address, port);
						System.out.println("Last Packet!");
						System.out.println("Offset:"+i);
						System.out.println("Packet data Size:"+size);
						socket.send(packet);
						i = i+MSS;
					}
				}	
				}
				System.out.println("Client done!");
				is.close();
				socket.close();
				ackSocket.close();
			}
		 catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

