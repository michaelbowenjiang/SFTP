import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;


public class Sftpserver {
	
		public static void main(String[] args){
			
			//declarations
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
			
			
			try {
				socket = new DatagramSocket(port);
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			try {
				ackSocket = new DatagramSocket();
				}catch (IOException e) {
				e.printStackTrace();
			}
			try {
				address = InetAddress.getByName("localhost");
				} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				
				packet = new DatagramPacket(buffer, buffer.length);
				while(true)
				{
					socket.receive(packet);			          
				try {
					
					
					os = new FileOutputStream(newFile,true);
					
					buffer = packet.getData();
					ByteBuffer buf = ByteBuffer.allocate(buffer.length);
					buf.put(buffer);
					int seqno = buf.getInt(0);
					buf.position(4);
					buf.get(receivingBuffer);
					int length = (int)newFile.length();
					
					
					System.out.println("Sequence number"+seqno);
					System.out.println("Offset:"+length);
					System.out.println("Length"+(packet.getLength()-4));
					
					ackNo = seqno;
					ackBuf.putInt(0,ackNo);
					ackBuffer = ackBuf.array();
					ackPacket = new DatagramPacket(ackBuffer,0,4,address,7736);
					
					
					os.write(packet.getData(),4,packet.getLength()-4);
					os.close();
					ackSocket.send(ackPacket);
					
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}}catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}


